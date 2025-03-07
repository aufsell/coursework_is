<template>
    <section>
      
      <div class="xz">
        <img src="../assets/beerlogo.png" alt="Кружка пива">
        <div class="description">
          <h2><b>Популярное пиво</b></h2>
          <p>Самое рейтинговое пиво всех времён</p>
        </div>
      </div>
      <div v-if=true>
        <p>Загрузка...</p>
      </div>
      <div v-else class="beer-grid">

      </div>
    </section>
  </template>