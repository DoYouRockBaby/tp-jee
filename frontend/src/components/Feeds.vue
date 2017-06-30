<template>
  <p v-if="loading" >Chargement des tweets en cours…</p>
  <ul v-else >
    <li v-for="tweet in tweets" class="tweet" ><tweet @retweeted="retweet" :tweet="tweet" :utilisateur="utilisateur" /></li>
  </ul>
</template>

<script>
import Tweet from './Tweet'
import Vue from 'vue'
import Resource from 'vue-resource'
Vue.use(Resource)

export default {
  name: 'feeds',
  components: {Tweet},
  props: ['tweets', 'utilisateur', 'loading'],
  methods: {
    retweet: function (id) {
      this.$emit('retweeted', id)
    }
  }
}
</script>

<style>
li.tweet:first-child {
 border-top-width: 1px;
}

li.tweet {
 border-style: solid;
 border-width: 0 1px 1px 1px;
 border-color: gray;
 width: 500px;
 margin: auto;
}

ul {
  list-style-type: none;
  padding: 0;
}

li {
  display: block;
  margin: 0 10px;
}
</style>
