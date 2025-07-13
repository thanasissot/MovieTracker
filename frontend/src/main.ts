import { createApp } from 'vue'
import './style.css'
import App from './App.vue'
import router from "./route.ts";
import PrimeVue from 'primevue/config';
import Aura from '@primeuix/themes/aura';

createApp(App).use(router).use(PrimeVue, {
    theme: {
        preset: Aura,
        options: {
            prefix: 'p',
            darkModeSelector: '.p-dark',
            cssLayer: false,
        }
    }
}).mount('#app')
