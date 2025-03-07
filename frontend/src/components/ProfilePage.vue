<template>
  <Header />

  <main class="body">
    <ProfileInfo />

    <section class="favourites-beers">
      <br />
      <br />
      <br />
      <br />
      <br />
      <br />
      <br />
      <br />
      <br />
      <br />
      <br />
    </section>

    <section class="profile-info">
      <div class="profile-card">
        <h3 class="section-title">Last Reviews</h3>
      </div>
      <br />
      <br />
      <br />
      <br />
      <br />
      <br />
      <br />
      <br />
      <br />
      <br />
      <br />
    </section>
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

.stats {
  display: flex;
  justify-content: center;
  gap: 20px;
}
</style>

<script>
import Header from "./Header.vue";
import ProfileInfo from "./ProfileInfo.vue";

export default {
  components: {
    Header,
    ProfileInfo,
  },
};
</script>
