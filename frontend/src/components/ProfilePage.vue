<template>
  <main class="body">
    <div class="header">
      <Header />
    </div>

    <ProfileInfo :profileUserId="profileUserId" />

    <FavouriteBeers :profileUserId="profileUserId" />

    <LastReviews :profileUserId="profileUserId" />
  </main>
</template>

<style scoped>
.main {
  background-color: red;
  position: absolute;
}
.body {
  display: flex;
  flex-direction: column;
  width: 100vw;
}

.profile-info {
  text-align: center;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #ffc473;
  width: 100%;
  position: relative;
  bottom: 2%;
  flex-direction: column;
  padding-bottom: 2vw;
  font-size: 27px;
}

.latest-reviews {
  text-align: center;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #ffc473;
  width: 100%;
  position: relative;
  bottom: 2%;
  flex-direction: column;
  padding-bottom: 2vw;
  font-size: 27px;
}

.stats {
  display: flex;
  justify-content: center;
  gap: 20px;
}

.header {
  width: 43vw;
  margin-left: 28.5%;
}
</style>

<script>
import Header from "./Header.vue";
import ProfileInfo from "./ProfileInfo.vue";
import FavouriteBeers from "./FavouriteBeers.vue";
import LastReviews from "./ProfileLasrReviews.vue";

export default {
  components: {
    Header,
    ProfileInfo,
    FavouriteBeers,
    LastReviews,
  },
  computed: {
    profileUserId() {
      return this.$route.params.profileUserId;
    },
  },
  watch: {
    profileUserId(newId, oldId) {
      if (newId !== oldId) {
        this.$emit("update");
      }
    },
  },
};
</script>
