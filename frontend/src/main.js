import { createApp } from "vue";
import App from "./App.vue";

import { createRouter, createWebHistory } from "vue-router";
import "bootstrap/dist/css/bootstrap.min.css";
import "bootstrap/dist/js/bootstrap.bundle.min.js";
import LoginPage from "./components/LoginPage.vue";
import RegisterPage from "./components/RegisterPage.vue";

const routes = [
  { path: "/", component: LoginPage },
  { path: "/login", component: LoginPage },
  { path: "/register", component: RegisterPage },
  { path: "/mainpage", component: () => import("./components/MainPage.vue") },
  {
    path: "/beer",
    component: () => import("./components/BeerDetail.vue"),
    props: true,
  },
  {
    path: "/profile/:profileUserId",
    component: () => import("./components/ProfilePage.vue"),
  },
  {
    path: "/profile/:profileUserId/subscribers",
    component: () => import("./components/Subscribers.vue"),
    props: true,
  },
  {
    path: "/profile/:profileUserId/followers",
    component: () => import("./components/Folowers.vue"),
    props: true,
  },
  {
    path: "/profile/:profileUserId/edit",
    component: () => import("./components/EditProfile.vue"),
    props: true,
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

router.beforeEach((to, from, next) => {
  if (to.path === "/profile") {
    const currentUserId = localStorage.getItem("userId");
    if (currentUserId) {
      next({ path: `/profile/${currentUserId}`, replace: true });
    } else {
      next("/login");
    }
  } else {
    next();
  }
});

createApp(App).use(router).mount("#app");
