<template>
  <div class="post-tweet">
    <textarea v-model="message" placeholder="Ecrire un tweet"></textarea><br/>
    <input @click="tweet(message)" type="submit" value="Tweet !" />
  </div>
</template>

<script>
import Vue from 'vue'
import Resource from 'vue-resource'
Vue.use(Resource)

export default {
  name: 'postTweet',
  props: ['utilisateur'],
  data () {
    return {
      message: []
    }
  },
  methods: {
    tweet: function (message) {
      console.log(this.utilisateur)
      this.$http.get('http://localhost:8080/tweet', {params: {auteur: this.utilisateur.handle, contenu: message}}).then(response => {
        this.$emit('tweeted', response.body)
      }, response => {
      })
    }
  }
}
</script>
