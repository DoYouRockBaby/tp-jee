<template>
  <div ui-view="tweet">
    <div class="column" >
      <img :src="avatar"/>
    </div>
    <div class="column" >
      <div><strong>{{ tweet.auteur.prenom }} {{ tweet.auteur.nom }}</strong> <span>{{ tweet.auteur.handle }} - {{ moment(tweet.date) }}</span></div>
      <div>{{ tweet.contenu }}</div>
      <div>
        <ul>
          <li class="button" ><icon name="reply" /></li>
          <li class="button" ><a @click="retweet(tweet.id, utilisateur)" ><icon name="retweet" /> {{ tweet.retweeters.length }}</a></li>
          <li class="button" ><icon name="heart" /></li>
          <li class="button" ><icon name="envelope" /></li>
        </ul>
      </div>
     </div>
  </div>
</template>

<script>
import 'vue-awesome/icons'
import Icon from 'vue-awesome/components/Icon'
import moment from 'moment'
import Vue from 'vue'
import Resource from 'vue-resource'
Vue.use(Resource)

export default {
  name: 'tweet',
  components: {Icon},
  props: ['tweet', 'utilisateur'],
  created () {
    moment.locale('fr')
  },
  computed: {
    avatar: function () {
      return 'https://robohash.org/' + this.tweet.auteur.handle + '?size=100x100'
    }
  },
  methods: {
    retweet: function (id, utilisateur) {
      if (utilisateur.handle === this.tweet.auteur.handle) {
        return
      }

      var action = 'retweet'
      for (let i = 0; i < this.tweet.retweeters.length; i++) {
        if (this.tweet.retweeters[i].handle === utilisateur.handle) {
          action = 'unretweet'
          break
        }
      }

      this.$http.get('http://localhost:8080/' + action, {responseType: 'text', params: {utilisateur: utilisateur.handle, tweet: this.tweet.id}}).then(response => {
        this.$emit('retweeted', id)
      }, response => {
      })
    },
    moment: function (date) {
      return moment(date).fromNow()
    }
  }
}
</script>

<style>
li.button {
 display: inline-block;
}

a {
 color: #42b983;
}

span.handle {
 color: gray;
}

div.column {
  display: inline-block;
}
</style>
