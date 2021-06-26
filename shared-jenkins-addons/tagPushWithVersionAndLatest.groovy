def call(String imgName,String version =""){
    sh "docker tag springcommunity/${imgName}:${version} ptclnc.azurecr.io/${imgName}:${version}"
    sh "docker push  ptclnc.azurecr.io/${imgName}:${version}"
    sh "docker tag springcommunity/${imgName}:${version} ptclnc.azurecr.op/${imgName}:latest"
    sh "docker push  ptclnc.azurecr.io/${imgName}:latest"
}
