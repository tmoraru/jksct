// Install multiple packages on an different Machine(Agent)
// Epel-release
// Git
// Ansible
// Terraform
// Packer

properties([
    parameters([
        string(defaultValue: '', description: 'Please enter VM IP Address ', name: 'nodeIP', trim: true)])])

node {
    withCredentials([
        sshUserPrivateKey(credentialsId: 'jenkins-master-ssh-key', keyFileVariable: 'SSHKEY', passphraseVariable: '', usernameVariable: 'SSHUSERNAME')]) 
            {
    // some block

     stage('Init') {
        sh 'ssh -o StrictHostKeyChecking=no -i  $SSHKEY  $SSHUSERNAME@${nodeIP} yum install epel-release -y'
    }

    stage('Install git') {
        sh 'ssh -o StrictHostKeyChecking=no -i $SSHKEY  $SSHUSERNAME@${nodeIP} yum install git -y'
    }

    stage('Install Java') {
        sh 'ssh -o StrictHostKeyChecking=no -i $SSHKEY  $SSHUSERNAME@${nodeIP} yum install java-1.8.0-openjdk-devel  -y'
    }
}
}

