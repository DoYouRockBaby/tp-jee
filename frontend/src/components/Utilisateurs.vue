<template>
  <div class="post-tweet">
    <label for="utilisateur" >Sélection de l’utilisateur connecté</label> :
    <select id="utilisateur" @change="utilisateurChanged($event.target.value)">
     <option disabled value="">Choisissez un utilisateur ...</option>
     <option v-for="utilisateur in utilisateurs" :value="utilisateur.handle">
       {{ utilisateur.prenom }} {{ utilisateur.nom }}
     </option>
    </select>
  </div>
</template>

<script>
import Vue from 'vue'
import Resource from 'vue-resource'
Vue.use(Resource)

export default {
  name: 'postTweet',
  props: ['utilisateurs'],
  data () {
    return {
      utilisateurs: []
    }
  },
  methods: {
    utilisateurChanged: function (handle) {
      this.$emit('utilisateurChanged', handle)
    }
  }
}
</script>
