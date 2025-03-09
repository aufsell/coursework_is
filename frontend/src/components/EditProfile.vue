<template>
  <section class="edit-profile" v-if="profile">
    <div class="profile-card">
      <div class="input-group">
        <label for="firstName">Имя:</label>
        <input
          v-model="profile.firstName"
          id="firstName"
          type="text"
          placeholder="Введите имя"
        />
      </div>

      <div class="input-group">
        <label for="lastName">Фамилия:</label>
        <input
          v-model="profile.lastName"
          id="lastName"
          type="text"
          placeholder="Введите фамилию"
        />
      </div>

      <div class="input-group">
        <label for="country">Страна:</label>
        <input
          v-model="profile.country"
          id="country"
          type="text"
          placeholder="Введите страну"
        />
      </div>

      <div class="input-group">
        <label for="email">Почта:</label>
        <input
          v-model="profile.email"
          id="email"
          type="email"
          placeholder="Введите почту"
          @input="validateEmail"
        />
        <p v-if="emailError" class="error-message">{{ emailError }}</p>
      </div>

      <div class="input-group">
        <label for="avatar">Аватар (.png):</label>
        <input
          type="file"
          id="avatar"
          accept=".png"
          @change="handleFileUpload"
        />
        <p v-if="avatarError" class="error-message">{{ avatarError }}</p>
      </div>

      <button
        class="upload-btn"
        @click="uploadAvatar"
        :disabled="!selectedFile"
      >
        Загрузить аватар
      </button>

      <button
        class="save-btn"
        @click="updateProfile"
        :disabled="isSaving || !isFormValid"
      >
        Сохранить
      </button>

      <p v-if="errorMessage" class="error-message">{{ errorMessage }}</p>
    </div>
  </section>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      profile: {
        firstName: "",
        lastName: "",
        country: "",
        email: "",
      },
      isSaving: false,
      errorMessage: "",
      emailError: "",
      selectedFile: null,
      avatarError: "",
    };
  },
  computed: {
    profileUserId() {
      return this.$route.params.profileUserId;
    },
    currentUserId() {
      return localStorage.getItem("userId");
    },
    isFormValid() {
      this.validateEmail();
      return (
        this.profile &&
        this.profile.firstName &&
        this.profile.lastName &&
        this.profile.country &&
        (this.profile.email === "" || !this.emailError)
      );
    },
  },
  async created() {
    if (this.currentUserId && this.profileUserId === this.currentUserId) {
      try {
        const response = await axios.get(
          `http://localhost:7777/api/v1/profile/${this.profileUserId}`,
          { withCredentials: true }
        );
        this.profile = {
          firstName: response.data.firstName || "",
          lastName: response.data.lastName || "",
          country: response.data.country || "",
          email: response.data.email || "",
        };
      } catch (error) {
        console.error("Ошибка при загрузке профиля:", error);
      }
    } else {
      this.$router.push("/login");
    }
  },
  methods: {
    validateEmail() {
      const emailPattern = /^[a-zA-Z0-9._%+-]+@([a-zA-Z0-9.-]+\.[a-zA-Z]{2,})$/;
      const validDomains = [
        "ifmo.ru",
        "mail.ru",
        "yandex.ru",
        "gmail.com",
        "example.com",
        "mydomain.org",
      ];
      const email = this.profile.email;

      if (email === "") {
        this.emailError = "";
        return true;
      }

      if (!emailPattern.test(email)) {
        this.emailError = "Некорректный формат почты.";
        return false;
      }

      const domain = email.split("@")[1];
      if (!validDomains.includes(domain)) {
        this.emailError = "Почта должна быть с одним из допустимых доменов.";
        return false;
      }

      this.emailError = "";
      return true;
    },
    handleFileUpload(event) {
      const file = event.target.files[0];

      if (!file) {
        this.avatarError = "Выберите файл.";
        this.selectedFile = null;
        return;
      }

      if (!file.name.toLowerCase().endsWith(".png")) {
        this.avatarError = "Файл должен быть в формате .png";
        this.selectedFile = null;
        return;
      }

      this.avatarError = "";
      this.selectedFile = file;
    },
    async uploadAvatar() {
      if (!this.selectedFile) {
        this.avatarError = "Выберите файл.";
        return;
      }

      const formData = new FormData();
      formData.append("file", this.selectedFile);

      try {
        await axios.post(
          `http://localhost:7777/api/v1/profile/${this.profileUserId}/avatar`,
          formData,
          {
            withCredentials: true,
            headers: { "Content-Type": "multipart/form-data" },
          }
        );
        alert("Аватар загружен успешно!");
        this.selectedFile = null;
      } catch (error) {
        this.avatarError = "Ошибка загрузки. Попробуйте снова.";
        console.error("Ошибка при загрузке аватара:", error);
      }
    },
    async updateProfile() {
      if (!this.validateEmail()) {
        return;
      }

      this.isSaving = true;
      this.errorMessage = "";

      try {
        const response = await axios.put(
          `http://localhost:7777/api/v1/profile/${this.profileUserId}`,
          this.profile,
          { withCredentials: true }
        );
        if (response.status === 200) {
          this.$router.push(`/profile/${this.profileUserId}`);
        }
      } catch (error) {
        this.errorMessage = "Ошибка при сохранении профиля. Попробуйте позже.";
        console.error("Ошибка при обновлении профиля:", error);
      } finally {
        this.isSaving = false;
      }
    },
  },
};
</script>

<style scoped>
.profile-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 80vw;
  gap: 1.5vw;
}

.input-group {
  display: flex;
  flex-direction: column;
  width: 35vw;
  margin: 1.5vw 0;
}

.input-group label {
  font-size: 1.2rem;
  margin-bottom: 0.5vw;
}

.input-group input {
  padding: 10px 20px;
  font-size: 1rem;
  width: 100%;
  border-radius: 60px;
  border: 1px solid rgba(0, 0, 0, 0.23);
  box-sizing: border-box;
}

.save-btn,
.upload-btn {
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

.upload-btn {
  background-color: #007bff;
}

.upload-btn:hover:not(:disabled) {
  background-color: #0056b3;
}

.save-btn:hover:not(:disabled) {
  background-color: #45a049;
}

.upload-btn:disabled,
.save-btn:disabled {
  background-color: #aaa;
  cursor: not-allowed;
}

.error-message {
  color: red;
  margin-top: 1vw;
  text-align: center;
}
</style>
