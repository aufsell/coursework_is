<template>
  <div class="upload-container">
    <h2>Загрузка файлов</h2>

    <div class="file-input">
      <label for="zipFile">Выберите ZIP-архив (photos.zip):</label>
      <input
        type="file"
        id="zipFile"
        @change="handleFileUpload('zip', $event)"
        accept=".zip"
      />
    </div>

    <div class="file-input">
      <label for="xlsxFile">Выберите Excel-файл (file.xlsx):</label>
      <input
        type="file"
        id="xlsxFile"
        @change="handleFileUpload('xlsx', $event)"
        accept=".xlsx"
      />
    </div>

    <button @click="submitFiles" :disabled="!zipFile || !xlsxFile">
      Отправить
    </button>

    <p v-if="message" class="message">{{ message }}</p>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      zipFile: null,
      xlsxFile: null,
      message: "",
    };
  },
  methods: {
    handleFileUpload(type, event) {
      const file = event.target.files[0];
      if (type === "zip") {
        this.zipFile = file;
      } else if (type === "xlsx") {
        this.xlsxFile = file;
      }
    },
    async submitFiles() {
      if (!this.zipFile || !this.xlsxFile) {
        this.message = "Выберите оба файла перед отправкой!";
        return;
      }

      const formData = new FormData();
      formData.append("photos", this.zipFile);
      formData.append("file", this.xlsxFile);

      try {
        const response = await axios.post(
          "http://localhost:7777/api/v1/admin/import",
          formData,
          {
            headers: { "Content-Type": "multipart/form-data" },
            withCredentials: true,
          }
        );
        console.log(response.data);
        this.message = "Файлы успешно загружены!";
      } catch (error) {
        console.error("Ошибка загрузки:", error);
        this.message = "Ошибка при загрузке файлов!";
      }
    },
  },
};
</script>

<style scoped>
.upload-container {
  display: flex;
  flex-direction: column;
  max-width: 400px;
  margin: 20px auto;
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 8px;
  background: #f9f9f9;
}

.file-input {
  margin-bottom: 15px;
}

button {
  padding: 10px;
  background: #007bff;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

button:disabled {
  background: #ccc;
  cursor: not-allowed;
}

.message {
  margin-top: 10px;
  font-weight: bold;
}
</style>
