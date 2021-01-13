node{
    stage("Pull Repo"){
        git branch: 'solution', changelog: false, poll: false, url: 'https://github.com/ikambarov/terraform-task.git'
    }
    stage("Terraform Init"){
        ssh ' terraform-0.13 version'
        
    }

    stage("Pull Terraform Apply"){
        ssh 'echo "Terraform Apply"'
    }
    
}


        