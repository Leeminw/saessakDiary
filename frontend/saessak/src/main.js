import { createApp } from 'vue';
import { createPinia } from 'pinia';
import App from './App.vue';
import router from './router/index.js';
import 'tailwindcss/tailwind.css';
import piniaPersist from 'pinia-plugin-persistedstate';
import { setupCalendar, Calendar, DatePicker } from 'v-calendar';
import 'v-calendar/style.css';
import VueCal from 'vue-cal';
import 'vue-cal/dist/vuecal.css';
import '@/assets/style.css';
import '@/assets/tailwind.css';

const app = createApp(App);

app.use(router);
// Use plugin defaults (optional)
app.use(setupCalendar, {});
// pinia
const pinia = createPinia();
pinia.use(piniaPersist);
app.use(pinia);

// Use the components
app.component('VCalendar', Calendar);
app.component('VDatePicker', DatePicker);
app.component('VueCal', VueCal);
app.mount('#app');
