<template>
    <div class="container mt-5">
      <h1 class="text-center mb-4">Register</h1>
      <div class="row justify-content-center">
        <div class="col-md-4">
          <form @submit.prevent="register" class="border p-4 rounded shadow-sm">
            <div class="mb-3">
              <label for="username" class="form-label">Username</label>
              <input 
                type="text" 
                v-model="username" 
                id="username" 
                class="form-control" 
                placeholder="Username" 
                required 
              />
            </div>
            <div class="mb-3">
              <label for="password" class="form-label">Password</label>
              <input 
                type="password" 
                v-model="password" 
                id="password" 
                class="form-control" 
                placeholder="Password" 
                required 
              />
            </div>
            <div class="mb-3">
              <div ref="recaptcha"></div>
            </div>
            <button type="submit" class="btn btn-primary w-100">Register</button>
          </form>
          <div class="text-center mt-3">
            <router-link to="/login">Already have an account? Login</router-link>
          </div>
          <div v-if="errorMessage" class="alert alert-danger mt-3" role="alert">
            {{ errorMessage }}
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  import axios from 'axios';
  
  export default {
    data() {
      return {
        username: '',
        password: '',
        errorMessage: '',
        siteKey: '6LdDbugqAAAAAIlh4EnSUbTlFZY25uJzCJy3wle-', // Замените на ваш Site Key
        widgetId: null,
        recaptchaToken: '',
        recaptchaScriptLoaded: false,
      };
    },
    mounted() {
      this.loadRecaptchaScript();
    },
    beforeUnmount() {
      if (window.grecaptcha && this.widgetId !== null) {
        this.resetRecaptcha();
      }
    },
    methods: {
      loadRecaptchaScript() {
        // Проверяем, не загружен ли скрипт ранее
        if (document.querySelector('script[src="https://www.google.com/recaptcha/api.js?render=explicit"]')) {
          this.waitForRecaptchaLoad();
          return;
        }
  
        const script = document.createElement('script');
        script.src = 'https://www.google.com/recaptcha/api.js?render=explicit';
        script.async = true;
        script.defer = true;
        script.onload = () => {
          this.recaptchaScriptLoaded = true;
          this.waitForRecaptchaLoad(); // Ждём загрузки перед рендерингом
        };
        script.onerror = () => {
          this.errorMessage = 'Failed to load reCAPTCHA. Please try again later.';
        };
        document.head.appendChild(script);
      },
      waitForRecaptchaLoad() {
        if (window.grecaptcha && window.grecaptcha.render) {
          this.recaptchaScriptLoaded = true;
          this.renderRecaptcha();
        } else {
          setTimeout(this.waitForRecaptchaLoad, 100);
        }
      },
      renderRecaptcha() {
        if (!this.recaptchaScriptLoaded || !window.grecaptcha || !this.$refs.recaptcha) {
          console.error('reCAPTCHA not ready yet');
          setTimeout(() => this.renderRecaptcha(), 100); // Повторная попытка через 100 мс
          return;
        }
        if (this.widgetId === null) {
          try {
            this.widgetId = window.grecaptcha.render(this.$refs.recaptcha, {
              sitekey: this.siteKey,
              callback: (response) => this.onRecaptchaVerified(response),
              'expired-callback': () => this.onRecaptchaExpired(),
              'error-callback': () => {
                this.errorMessage = 'reCAPTCHA error occurred. Please try again.';
              },
            });
          } catch (error) {
            console.error('Error rendering reCAPTCHA:', error);
            this.errorMessage = 'Failed to initialize reCAPTCHA.';
          }
        }
      },
      onRecaptchaVerified(response) {
        this.recaptchaToken = response;
      },
      onRecaptchaExpired() {
        this.recaptchaToken = '';
        if (window.grecaptcha && this.widgetId !== null) {
          window.grecaptcha.reset(this.widgetId);
        }
      },
      resetRecaptcha() {
        if (window.grecaptcha && this.widgetId !== null) {
          window.grecaptcha.reset(this.widgetId);
          this.widgetId = null;
        }
        this.recaptchaToken = '';
      },
      async register() {
        this.errorMessage = '';
  
        // Валидация логина и пароля
        if (this.username.length < 5) {
          this.errorMessage = 'Username must be more than 5 characters.';
          return;
        }
        if (this.password.length < 8) {
          this.errorMessage = 'Password must be more than 8 characters.';
          return;
        }
  
        if (!this.recaptchaScriptLoaded || !window.grecaptcha) {
          this.errorMessage = 'reCAPTCHA is not loaded yet. Please wait.';
          return;
        }
  
        const recaptchaResponse = window.grecaptcha.getResponse(this.widgetId);
        if (!recaptchaResponse) {
          this.errorMessage = 'Please complete the reCAPTCHA.';
          return;
        }
  
        try {
          const response = await axios.post('http://localhost:7777/auth/signup', {
            username: this.username,
            password: this.password,
            'g-recaptcha-response': recaptchaResponse,
          });
          
          if (response.status === 201) {
            this.$router.push('/login');
          }
        } catch (error) {
          if (error.response) {
            this.errorMessage = error.response.data || 'Registration failed! Please try again.';
          } else {
            this.errorMessage = 'Registration failed! Please check your network connection.';
          }
        }
      },
    },
  };
  </script>
  
  