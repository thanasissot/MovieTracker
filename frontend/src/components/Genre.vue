<script setup lang="ts">
import {
  FlexRender,
  getCoreRowModel,
  useVueTable,
  createColumnHelper,
} from '@tanstack/vue-table'
import { ref } from 'vue'

type Genre = {
  id: number
  name: string
}

</script>