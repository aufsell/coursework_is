<template>
  <section class="profile-info">
    <div class="profile-card">
      <div class="avatar"></div>
      <div><h1 class="username">Пивной Гений</h1></div>
      <div class="stat-box-items">
        <span class="stat-label">Reviews</span>
        <span class="stat-value">2</span>
      </div>
      <button class="follow-btn">Follow</button>
      <div class="stat-box">
        <div class="stat-box-items-inner">
          <span class="stat-label">Followers</span>
          <span class="stat-value">4</span>
        </div>
        <div class="stat-box-arrow">
          <img src="../assets/arrow.png" alt="раскрыть" />
        </div>
      </div>
      <div class="stat-box">
        <div class="stat-box-items-inner">
          <span class="stat-label">Followings</span>
          <span class="stat-value">5</span>
        </div>
        <div class="stat-box-arrow">
          <img src="../assets/arrow.png" alt="раскрыть" />
        </div>
      </div>
    </div>
  </section>
</template>

<style scoped>
.stat-box {
  display: flex;
  flex-direction: row;
  align-items: center;
  stroke: black;
  stroke-width: 2px;
  border: 1px solid rgba(0, 0, 0, 0.23);
  width: 40vw;
  box-shadow: 0px 4px 4px rgba(0, 0, 0, 0.25);
}

.stat-box-items {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 30vw;
}

.stat-box-items-inner {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-left: 2vw;
  width: 10vw;
}

.stat-box-arrow {
  margin-left: 24vw;
}

.profile-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 80vw;
  gap: 1.5vw;
}

.avatar {
  width: 140px;
  height: 140px;
  background: gray;
  border-radius: 50%;
  margin: 0 auto;
}

.follow-btn {
  background-color: white;
  color: black;
  padding: 10px 20px;
  border: none;
  cursor: pointer;
  border-radius: 60px;
  width: 35vw;
  stroke: black;
  stroke-width: 2px;
  border: 1px solid rgba(0, 0, 0, 0.23);
}
</style>
