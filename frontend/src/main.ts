import { createApp } from 'vue'
import './style.css'
import App from './App.vue'
import router from "./route.ts";
import PrimeVue from 'primevue/config';
import Aura from '@primeuix/themes/aura';
import {ToastService} from "primevue";
import DialogService from 'primevue/dialogservice'


createApp(App)
    .use(ToastService)
    .use(DialogService)
    .use(router).use(PrimeVue, {
    theme: {
        preset: Aura,
        options: {
            prefix: 'p',
            darkModeSelector: '.p-dark',
            cssLayer: false,
        }
    }
}).mount('#app')
