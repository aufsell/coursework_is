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
      <button class="follow-btn">Follow</button>
      <div class="stat-box">
        <div class="stat-box-items-inner">
          <span class="stat-label">Followers</span>
          <span class="stat-value">{{ followersCount }}</span>
        </div>
        <div class="stat-box-arrow">
          <img src="../assets/arrow.png" alt="раскрыть" />
        </div>
      </div>
      <div class="stat-box">
        <div class="stat-box-items-inner">
          <span class="stat-label">Followings</span>
          <span class="stat-value">{{ followingsCount }}</span>
        </div>
        <div class="stat-box-arrow">
          <img src="../assets/arrow.png" alt="раскрыть" />
        </div>
      </div>
    </div>
  </section>
</template>

<script>
import axios from "axios";

export default {
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
      reviewsCount: 1000,
      followingsCount: 0,
    };
  },
  computed: {
    profileUserId() {
      return this.$route.params.profileUserId;
    },
  },
  async created() {
    if (!this.profileUserId) return;

    try {
      const response = await axios.get(
        `http://localhost:7777/api/v1/profile/${this.profileUserId}`,
        { withCredentials: true }
      );
      this.profile = response.data;
    } catch (error) {
      console.error("Ошибка при загрузке профиля:", error);
    }

    try {
      const response = await axios.get(
        `http://localhost:7777/api/v1/profile/subscribers/${this.profileUserId}/count`,
        { withCredentials: true }
      );
      this.followersCount = response.data;
    } catch (error) {
      console.error("Ошибка при запросе количества подписчиков:", error);
    }

    try {
      const response = await axios.get(
        `http://localhost:7777/api/v1/profile/subscribed/${this.profileUserId}/count`,
        { withCredentials: true }
      );
      this.followingsCount = response.data;
    } catch (error) {
      console.error("Ошибка при запросе количества подписок:", error);
    }

    try {
      const response = await axios.get(
        `http://localhost:7777/api/v1/reviews/user/${this.profileUserId}/count`,
        { withCredentials: true }
      );
      this.reviewsCount = response.data;
    } catch (error) {
      console.error("Ошибка при запросе количества отзывов:", error);
    }
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
  stroke: black;
  stroke-width: 2px;
  border: 1px solid rgba(0, 0, 0, 0.23);
}
</style>
