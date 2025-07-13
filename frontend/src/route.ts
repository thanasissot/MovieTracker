import { createMemoryHistory, createRouter } from 'vue-router'
import HelloWorld from "./components/HelloWorld.vue";
import Genre from "./components/Genre.vue";

const routes = [
  { path: '/', component: HelloWorld },
  { path: '/genres', component: Genre },
  { path: '/**', component: HelloWorld },

]
const router = createRouter({
  history: createMemoryHistory(),
  routes,
})

export default router
