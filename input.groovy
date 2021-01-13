node {
    withCredentials(
        [sshUserPrivateKey(
            credentialsId: 'jenkins-master-ssh-key', keyFileVariable: 'SSHKEY', passphraseVariable: '', usernameVariable: 'SSHUSERNAME')
            ]) 
            {
    // some block

     stage('Init') {
        sh 'ssh -i $SSHKEY  root@ip-address-of-an-agent yum install epel-release -y'
    }
}
}