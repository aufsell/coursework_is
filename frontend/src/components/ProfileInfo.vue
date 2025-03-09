<template>
  <section class="profile-info">
    <div class="profile-card">
      <div class="avatar">
        <img v-if="profile.avatarPath" :src="profile.avatarPath" alt="Avatar" />
      </div>
      <div>
        <h1 class="username">{{ profile.name || "Без имени" }}</h1>
      </div>
      <p v-if="profile.firstName || profile.lastName">
        {{ profile.firstName || "" }} {{ profile.lastName || "" }}
      </p>
      <p v-if="profile.country">Страна: {{ profile.country }}</p>

      <div class="stat-box-items">
        <span class="stat-label">Reviews</span>
        <span class="stat-value">{{ reviewsCount }}</span>
      </div>

      <button
        v-if="showFollowButton"
        class="follow-btn"
        @click="toggleSubscription"
      >
        {{ isSubscribed ? "Отписаться" : "Подписаться" }}
      </button>

      <div class="stat-box">
        <div class="stat-box-items-inner">
          <span class="stat-label">Подписки</span>
          <span class="stat-value">{{ followersCount }}</span>
        </div>
        <div class="stat-box-arrow">
          <img
            src="../assets/arrow.png"
            alt="раскрыть"
            @click="goToSubscribers"
          />
        </div>
      </div>
      <div class="stat-box">
        <div class="stat-box-items-inner">
          <span class="stat-label">Подписчики</span>
          <span class="stat-value">{{ followingsCount }}</span>
        </div>
        <div class="stat-box-arrow">
          <img
            src="../assets/arrow.png"
            alt="раскрыть"
            @click="goToFollowers"
          />
        </div>
      </div>

      <button
        v-if="currentUserId === profileUserId"
        class="edit-btn"
        @click="editProfile"
      >
        Редактировать
      </button>
    </div>
  </section>
</template>

<script>
import axios from "axios";

export default {
  name: "ProfileInfo",
  props: {
    profileUserId: {
      type: String,
      required: true,
    },
  },
  data() {
    return {
      profile: {
        name: "",
        firstName: "",
        lastName: "",
        country: "",
        avatarPath: "",
      },
      followersCount: 0,
      reviewsCount: 0,
      followingsCount: 0,
      isSubscribed: false,
    };
  },
  computed: {
    currentUserId() {
      return localStorage.getItem("userId");
    },
    showFollowButton() {
      return this.currentUserId && this.currentUserId !== this.profileUserId;
    },
  },
  watch: {
    profileUserId(newId, oldId) {
      if (newId !== oldId) {
        this.loadProfileData();
      }
    },
  },
  created() {
    this.loadProfileData();
  },
  methods: {
    async loadProfileData() {
      if (!this.profileUserId) return;

      try {
        const profileResponse = await axios.get(
          `http://localhost:7777/api/v1/profile/${this.profileUserId}`,
          { withCredentials: true }
        );
        this.profile = profileResponse.data;

        const followersResponse = await axios.get(
          `http://localhost:7777/api/v1/profile/subscribers/${this.profileUserId}/count`,
          { withCredentials: true }
        );
        this.followersCount = followersResponse.data;

        const followingsResponse = await axios.get(
          `http://localhost:7777/api/v1/profile/subscribed/${this.profileUserId}/count`,
          { withCredentials: true }
        );
        this.followingsCount = followingsResponse.data;

        const reviewsResponse = await axios.get(
          `http://localhost:7777/api/v1/reviews/user/${this.profileUserId}/count`,
          { withCredentials: true }
        );
        this.reviewsCount = reviewsResponse.data;

        if (this.currentUserId) {
          const subscriptionResponse = await axios.get(
            `http://localhost:7777/api/v1/profile/subscribed/${this.profileUserId}/isSubscribed`,
            { withCredentials: true }
          );
          this.isSubscribed = subscriptionResponse.data;
        }
      } catch (error) {
        console.error("Ошибка при загрузке данных профиля:", error);
      }
    },
    async toggleSubscription() {
      if (this.isSubscribed) {
        try {
          await axios.delete(
            `http://localhost:7777/api/v1/profile/subscribers/${this.profileUserId}`,
            { withCredentials: true }
          );
          this.isSubscribed = false;
          this.followingsCount--;
        } catch (error) {
          console.error("Ошибка при отписке:", error);
        }
      } else {
        try {
          await axios.post(
            `http://localhost:7777/api/v1/profile/subscribers/${this.profileUserId}`,
            {},
            { withCredentials: true }
          );
          this.isSubscribed = true;
          this.followingsCount++;
        } catch (error) {
          console.error("Ошибка при подписке:", error);
        }
      }
    },
    goToFollowers() {
      this.$router.push(`/profile/${this.profileUserId}/followers`);
    },
    goToSubscribers() {
      this.$router.push(`/profile/${this.profileUserId}/subscribers`);
    },
    editProfile() {
      this.$router.push(`/profile/${this.profileUserId}/edit`);
    },
  },
};
</script>

<style scoped>
.stat-box {
  display: flex;
  flex-direction: row;
  align-items: center;
  stroke: black;
  stroke-width: 2px;
  border: 1px solid rgba(0, 0, 0, 0.23);
  width: 40vw;
  box-shadow: 0px 4px 4px rgba(0, 0, 0, 0.25);
}

.stat-box-items {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 30vw;
}

.stat-box-items-inner {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-left: 2vw;
  width: 10vw;
}

.stat-box-arrow {
  margin-left: 24vw;
}

.profile-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 80vw;
  gap: 1.5vw;
}

.avatar {
  width: 140px;
  height: 140px;
  background: gray;
  border-radius: 50%;
  margin: 0 auto;
}

.follow-btn {
  background-color: white;
  color: black;
  padding: 10px 20px;
  border: none;
  cursor: pointer;
  border-radius: 60px;
  width: 35vw;
  border: 1px solid rgba(0, 0, 0, 0.23);
  transition: transform 0.2s ease, background-color 0.2s ease;
}

.follow-btn:hover {
  background-color: #f0f0f0;
}

.follow-btn:active {
  background-color: #dcdcdc;
}

.clicked {
  transform: scale(0.9);
}

.stat-box-arrow img {
  cursor: pointer;
}

.stat-box-arrow img:hover {
  transform: scale(1.3);
}

.edit-btn {
  background-color: #4caf50;
  color: white;
  padding: 10px 20px;
  border: none;
  cursor: pointer;
  border-radius: 60px;
  width: 35vw;
  margin-top: 20px;
  transition: background-color 0.2s ease;
}

.edit-btn:hover {
  background-color: #45a049;
}

.edit-btn:active {
  background-color: #388e3c;
}

.avatar {
  width: 140px;
  height: 140px;
  background: gray;
  border-radius: 50%;
  margin: 0 auto;
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
}

.avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
</style>
