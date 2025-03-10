<template>
  <div
    v-if="isOpen"
    class="fixed inset-0 flex items-center justify-center bg-black/50 z-50"
    @click="handleBackdropClick"
  >
    <div
      class="modal-content bg-white p-5 rounded-lg w-96 shadow-lg"
      @click.stop
    >
      <div class="flex justify-between items-center mb-4">
        <h2 class="text-lg font-semibold">Уведомления</h2>
      </div>

      <div class="max-h-60 overflow-y-auto">
        <p
          v-if="!loading && notifications.length === 0"
          class="text-center text-gray-500"
        >
          Нет уведомлений
        </p>

        <notification-item
          v-for="notification in notifications"
          :key="notification.notificationId"
          :notification="notification"
        />
      </div>
    </div>
  </div>
</template>

<script>
import NotificationItem from "./NotificationItem.vue";
import axios from "axios";
export default {
  name: "NotificationModal",
  components: {
    NotificationItem,
  },
  props: {
    isOpen: Boolean,
    onClose: Function,
  },
  data() {
    return {
      notifications: [],
      loading: true,
    };
  },
  watch: {
    isOpen(newVal) {
      if (newVal) {
        this.fetchNotifications();
      }
    },
  },
  methods: {
    async fetchNotifications() {
      try {
        const response = await axios.get(
          "http://localhost:7777/api/v1/notification",
          { withCredentials: true }
        );
        console.log("Ответ сервера:", response.data);

        if (Array.isArray(response.data)) {
          this.notifications = response.data.map((notification) => ({
            notificationId: notification.id,
            message: notification.message,
            createdAt: notification.created_at,
            userId: notification.user.profileId,
            userName: notification.user.name,
            userAvatarPath: notification.user.avatarPath,
          }));
        } else {
          console.error("Ответ от сервера не является массивом.");
          this.notifications = [];
        }
      } catch (error) {
        console.error("Ошибка загрузки уведомлений:", error);
        this.loading = false;
      }
    },
    handleBackdropClick(event) {
      if (event.target === event.currentTarget) {
        this.onClose();
      }
    },
  },
};
</script>

<style scoped>
.modal-content {
  transform: translateY(100px);
  transition: transform 0.3s ease-out;
  position: relative;
  width: 20vw;
  margin-left: 60vw;
}

.modal-content-enter-active {
  transform: translateY(0);
}

.modal-content-exit-active {
  transform: translateY(100px);
}

.fixed {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 50;
}

.max-h-60 {
  max-height: 13.8vw;
  overflow-y: auto;
}
</style>
