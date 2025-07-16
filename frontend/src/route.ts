import {createRouter, createWebHistory} from 'vue-router'
import HelloWorld from "./components/HelloWorld.vue";
import Genre from "./components/Genre.vue";
import Actor from "./components/Actor.vue";
import Movie from "./components/Movie.vue";

const routes = [
  { path: '/', component: HelloWorld },
  { path: '/genres', component: Genre },
  { path: '/actors', component: Actor },
  { path: '/movies', component: Movie },
  { path: '/**', component: HelloWorld },

]
const router = createRouter({
  history: createWebHistory(),
  routes,
})

export default router
