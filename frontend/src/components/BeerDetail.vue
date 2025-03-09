<template>
  <div v-if="beer">
    <img :src="beer.image_path" alt="">
    <h2>{{ beer.name }}</h2>
    <p><strong>abv:</strong> {{ beer.abv }}</p>
    <p><strong>Страна:</strong> {{ beer.country }}</p>
    <p><strong>Тип ферментации:</strong> {{ fermentationType }}</p>
    <p><strong>IBU:</strong> {{ beer.ibu }}</p>
    <p><strong>Цена:</strong> {{ beer.price }}</p>
    <p><strong>OG:</strong> {{ beer.og }}%</p>
    <p><strong>SRM:</strong> {{ beer.srm }}</p>
    <p><strong>Объем:</strong> {{ beer.volume }}</p>
  </div>
  <p v-else>Загрузка...</p>
</template>

<script>
import { ref, onMounted } from 'vue';
import axios from 'axios';

export default {
  props: {
    beerId: String
  },
  setup(props) {
    const beer = ref(null);
    const fermentationType = ref('');

    const fetchBeerData = async () => {
      try {
        // Выполняем первый запрос для получения пива
        const beerResponse = await axios.get(`http://localhost:7777/api/v1/beer/beers/search?id=${props.beerId}`, {
          withCredentials: true,
        });

        console.log('Данные пива:', beerResponse.data[0]);

        // Используем данные из первого запроса для второго запроса
        const fermentationResponse = await axios.get(`http://localhost:7777/api/v1/beer/fermentationType/${beerResponse.data[0].fermentation_type}`, {
          withCredentials: true,
        });

        // Присваиваем данные пива и типа брожения в реактивные переменные
        beer.value = beerResponse.data[0];
        fermentationType.value = fermentationResponse.data?.name || ''; // Поле, которое приходит из API

        console.log('Данные загружены:', beer.value, fermentationResponse.data.name);
      } catch (error) {
        console.error('Ошибка при загрузке данных:', error);
      }
    };

    onMounted(fetchBeerData);

    return { beer, fermentationType };
  },
};
</script>