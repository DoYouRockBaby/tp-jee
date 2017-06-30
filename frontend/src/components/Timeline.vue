<template>
  <div class="timeline">
    <postTweet :utilisateur="currentUtilisateur" @tweeted="tweeted" />
    <utilisateurs :utilisateurs="utilisateurs" @utilisateurChanged="utilisateurChanged" />
    <feeds @click="retweet()" :utilisateur="currentUtilisateur" :tweets="sortedTweets" :loading="loading" @retweeted="retweet" />
  </div>
</template>

<script>
import Feeds from './Feeds'
import Utilisateurs from './Utilisateurs'
import PostTweet from './PostTweet'
import Vue from 'vue'
import Resource from 'vue-resource'
Vue.use(Resource)

export default {
  name: 'timeline',
  data () {
    return {
      tweets: [],
      currentUtilisateur: null,
      loading: true
    }
  },
  components: {Feeds, Utilisateurs, PostTweet},
  created () {
    this.fetchTweets()
    this.fetchUtilisateurs()
  },
  computed: {
    sortedTweets: function () {
      return this.tweets.sort((a, b) => a.date < b.date)
    }
  },
  methods: {
    fetchTweets: function () {
      this.$http.get('http://localhost:8080/list').then(response => {
        this.tweets = response.body
        this.loading = false
      }, response => {
      })
    },
    fetchUtilisateurs: function () {
      this.$http.get('http://localhost:8080/utilisateurs').then(response => {
        this.utilisateurs = response.body
        this.utilisateurChanged(this.utilisateurs[0].handle)
      }, response => {
      })
    },
    retweet: function (id) {
      for (let i = 0; i < this.tweets.length; i++) {
        if (this.tweets[i].id === id) {
          this.$http.get('http://localhost:8080/tweet/' + id).then(response => {
            this.tweets[i].retweeters = response.body.retweeters
          }, response => {
          })
        }
      }
    },
    tweeted: function (tweet) {
      this.tweets.push(tweet)
    },
    utilisateurChanged: function (handle) {
      for (let i = 0; i < this.utilisateurs.length; i++) {
        if (this.utilisateurs[i].handle === handle) {
          this.currentUtilisateur = this.utilisateurs[i]

          this.loading = true
          this.$http.get('http://localhost:8080/feed/' + handle).then(response => {
            this.tweets = response.body
            this.loading = false
          }, response => {
          })
        }
      }
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
h1, h2 {
  font-weight: normal;
}

ul {
  list-style-type: none;
  padding: 0;
}

li {
  display: inline-block;
  margin: 0 10px;
}

a {
  color: #42b983;
}
</style>
