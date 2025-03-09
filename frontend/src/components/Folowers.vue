<template>
  <Header />

  <section class="subscribers-list">
    <h2>Подписчики</h2>
    <div class="subscribers-grid">
      <SubscriberCard
        v-for="subscriber in subscribers"
        :key="subscriber.profileId"
        :subscriber="subscriber"
      />
    </div>
  </section>
</template>

<script>
import axios from "axios";
import Header from "./Header.vue";
import SubscriberCard from "./SubscriberCard.vue";

export default {
  name: "SubscribersList",
  components: {
    SubscriberCard,
    Header,
  },
  data() {
    return {
      subscribers: [],
    };
  },
  computed: {
    profileUserId() {
      return this.$route.params.profileUserId;
    },
  },
  async created() {
    try {
      const response = await axios.get(
        `http://localhost:7777/api/v1/profile/subscribed/${this.profileUserId}`,
        { withCredentials: true }
      );
      console.log("Ответ сервера:", response.data);
      this.subscribers = response.data.map((subscriber) => ({
        userId: subscriber.profileId,
        username: subscriber.name,
        avatarPath: subscriber.avatarPath || "/default-avatar.png",
      }));
    } catch (error) {
      console.error("Ошибка при загрузке подписчиков:", error);
    }
  },
};
</script>

<style scoped>
.subscribers-list {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 1.5vw;
  width: 100%;
}

h2 {
  font-size: 20px;
}

.subscribers-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
  justify-content: center;
  width: 100%;
  max-width: 600px;
}
</style>
