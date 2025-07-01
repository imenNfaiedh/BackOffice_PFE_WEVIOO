pipeline {
    agent any

     tools {
        maven 'install maven' // Define Maven tool to be used
          }
  stages {
        stage("Clean up") {
            steps {
                deleteDir() // Clean up the workspace before starting
            }
        }
       stage("Checkout SCM") {
            steps {

                checkout scm

            }
        }
}
