node {
label 'slave 1' 
    withMaven(maven:'maven') {

        stage('Checkout') {
            git url: 'https://github.com/reshmigu/AtoBe_ver1.1.git', credentialsId: 'master', branch: 'master'
        }

		stage('Build') {
		    sh 'mvn package shade:shade'
            def pom = readMavenPom file:'pom.xml'
            env.version = pom.version
        }

        stage('Image') {
                sh 'docker stop restassured || true && docker rm restassured || true'
                cmd = "docker rmi restassured:${env.version} || true"
                sh cmd
                docker.build "restassured:${env.version}"
        }

        stage ('Run') {
            docker.image("restassured:${env.version}").run(' -h restassured --name restassured --net host')
        }

    }

}
