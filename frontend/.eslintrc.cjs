module.exports = {
    extends: ['eslint:recommended', 'plugin:vue/vue3-recommended'],
    env: {
      browser: true,
      es2021: true
    },
    globals: {
      defineProps: "readonly"
    }
  };
  