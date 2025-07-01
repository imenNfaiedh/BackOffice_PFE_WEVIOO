 pipeline {
     agent any

     tools {
         maven 'install maven' // Assure-toi que ce nom correspond à ce que tu as défini dans Jenkins
     }

     stages {

         stage("Clean up") {
             steps {
                 deleteDir() // Nettoyer le workspace avant de commencer
             }
         }

         stage("Checkout SCM") {
             steps {
                 checkout scm
             }
         }

            stage("Test project") {
                      steps {
                          dir("${env.WORKSPACE}") {
                              sh "mvn test"
                          }
                      }
                  }

         stage("Build project") {
             steps {
                 dir("${env.WORKSPACE}") {
                     sh "mvn clean install -DskipTests"
                 }
             }
         }


     }
 }
