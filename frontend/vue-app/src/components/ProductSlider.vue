<template>
  <section>
    <h2>{{ title }}</h2>
    <div class="slider">
      <div class="slide" v-for="p in items" :key="p.id">
        <div class="card">
          <h3>{{ p.title }}</h3>
          <p class="price">{{ formatPrice(p.price) }}</p>
        </div>
      </div>
    </div>
  </section>
</template>

<script>
export default {
  name: 'ProductSlider',
  props: {
    items: { type: Array, default: () => [] },
    title: { type: String, default: 'Products' }
  },
  methods: {
    formatPrice(v){ return typeof v === 'number' ? `$${v.toFixed(2)}` : v }
  }
}
</script>

<style scoped>
.slider{ display:flex; gap:1rem; overflow:auto; padding:0.5rem 0 }
.slide{ min-width:220px }
.card{ border:1px solid #e5e7eb; padding:0.75rem; border-radius:6px }
.price{ color:#ef4444 }
</style>
