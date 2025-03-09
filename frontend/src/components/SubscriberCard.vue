<template>
  <div class="subscriber-card" @click="goToProfile">
    <div class="avatar">
      <img :src="subscriber.avatarPath" :alt="subscriber.username" />
    </div>
    <div class="username">{{ subscriber.username }}</div>
  </div>
</template>

<script>
export default {
  name: "SubscriberCard",
  props: {
    subscriber: Object,
  },
  methods: {
    goToProfile() {
      this.$router.push({ path: `/profile/${this.subscriber.userId}` });
    },
  },
};
</script>

<style scoped>
.subscriber-card {
  display: flex;
  align-items: center;
  background-color: #fff;
  border-radius: 10px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  padding: 10px;
  cursor: pointer;
  transition: background 0.3s;
}

.subscriber-card:hover {
  background: #f5f5f5;
}

.avatar {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  overflow: hidden;
  margin-right: 10px;
}

.avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.username {
  font-size: 16px;
  font-weight: bold;
  color: #333;
}
</style>
