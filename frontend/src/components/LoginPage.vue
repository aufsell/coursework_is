<template>
  <div class="container mt-5">
    <div class="row login-container">
      <div class="col-md-5 zx">
        <img src=".././assets/logreg.png" class="img-fluid pipivo-image" alt="Pipivo Image">
      </div>
      <div class="col-md-7">
        <h1 class="login-title">Join Pipivo <span>| Sign In</span></h1>
        <form @submit.prevent="login" class="p-4">
          <div class="mb-3">
            <input
              v-model="username"
              class="form-control custom-input"
              placeholder="Type your email"
              required
            />
          </div>
          <div class="mb-3">
            <input
              type="password"
              v-model="password"
              class="form-control custom-input"
              placeholder="Type your password"
              required
            />
          </div>
          <div class="mb-3">
          <!-- Убираем класс g-recaptcha, чтобы избежать автоинициализации -->
          <div ref="recaptcha"></div>
        </div>
          <button type="submit" class="btn btn-success w-100 custom-button">Continue</button>
          <div v-if="errorMessage" class="alert alert-danger mt-3" role="alert">
            {{ errorMessage }}
          </div>
        </form>
        <div class="text-center mt-3">
          Don't have a profile? <router-link to="/register" class="text-orange">Join Pipivo</router-link>
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
        siteKey: '6LdUwesqAAAAAIquLTBYjuZZ1fuk07dMobzKRMNU',
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
    this.waitForRecaptchaLoad();
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
    setTimeout(() => this.renderRecaptcha(), 100);
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
      async login() {
        this.errorMessage = '';
  
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
          const response = await axios.post('http://localhost:7777/auth/signin', {
            username: this.username,
            password: this.password,
            recaptcha: recaptchaResponse,
          }, {
            withCredentials: true,
          });
  
          console.log(response.data);
          localStorage.setItem('userId', JSON.stringify(response.data.id));
          localStorage.setItem('username', JSON.stringify(response.data.username));
          localStorage.setItem('role', JSON.stringify(response.data.role));
  
          this.$router.push('/mainpage');
        } catch (error) {
          if (error.response) {
            this.errorMessage = error.response.data.message || 'Incorrect username or password, please try again';
          } else {
            this.errorMessage = 'Login failed! Please check your network connection.';
          }
        }
      },
    },
  };
  </script>
  
  <style scoped>
.login-container {
  border-radius: 30px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  background-color: white;
  margin-top: 13vh;
  padding: 0px;
}

/* Image styling */
.pipivo-image {
   margin-left: 0px;
   padding-left: 0px;
}

/* Title styling */
.login-title {
  color: #FF9400; /* Orange color */
  font-weight: bold;
  text-align: center;
  margin-bottom: 1.5rem;
}
.login-title span{
  color: #000000; /* Orange color */
  font-weight: bold;
  text-align: center;
  margin-bottom: 1.5rem;
}

.custom-input {
  background-color: #f8f9fa; 
  border: 1px solid #ced4da; 
  border-radius: 5px; 
}


.custom-button {
  border-radius: 20px; 
  font-weight: bold; 
}


.text-orange {
  color: #ff6200; 
}

.zx{
  display: flex;
  padding: 0px;
}


body {
  background-color: #f5f5f5; 
}
  </style>