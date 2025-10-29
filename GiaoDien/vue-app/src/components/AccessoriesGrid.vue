<template>
  <section>
    <h2>Accessories</h2>
    <div class="grid">
      <div class="card" v-for="item in items" :key="item.id">
        <h3>{{ item.name }}</h3>
        <p class="price">{{ formatPrice(item.price) }}</p>
      </div>
    </div>
  </section>
</template>

<script>
export default {
  name: 'AccessoriesGrid',
  props: {
    items: { type: Array, default: () => [] }
  },
  methods: {
    formatPrice(v){ return typeof v === 'number' ? `$${v.toFixed(2)}` : v }
  }
}
</script>

<style scoped>
.grid{ display:grid; grid-template-columns: repeat(auto-fill,minmax(160px,1fr)); gap:1rem }
.card{ border:1px solid #e5e7eb; padding:0.75rem; border-radius:6px }
.price{ color: #10b981; font-weight:600 }
</style>
