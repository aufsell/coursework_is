<template>
  <div
    class="notification-item flex items-center p-3 border-b last:border-none"
  >
    <a :href="`/profile/${notification.userId}`" class="mr-3">
      <img
        :src="notification.userAvatarPath"
        :alt="notification.userName"
        class="w-10 h-10 rounded-full border"
      />
    </a>
    <p class="text-sm text-gray-700">{{ notification.message }}</p>
  </div>
</template>

<script>
export default {
  name: "NotificationItem",
  props: {
    notification: {
      type: Object,
      required: true,
    },
  },
};
</script>

<style scoped>
.notification-item {
  display: flex;
  align-items: center;
  padding: 10px;
  border-bottom: 1px solid #ddd;
}

.notification-item img {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  margin-right: 10px;
}

.notification-item p {
  font-size: 14px;
  color: #333;
}
</style>
