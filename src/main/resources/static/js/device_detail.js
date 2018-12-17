var vm = new Vue({
    el: '#app',
    data: {
        pageName: 'deviceDetail',
        pageTitle: '设备详情',
        adminId: adminId,
        adminRoleId: adminRoleId,
        admin: admin,
        menuPer: menuPer,
        opPer: opPer,
        qrcodeDialogVisible: false,

        deviceId: null,
        device: null

    }, 

    mounted: function() {
        let href = window.location.href
        if (href.indexOf('?') != -1) {
            let query = href.split('?')[1].split('&')
            let param = {}
            query.forEach(function(item, index){
                let itemArrey = item.split('=')
                param[itemArrey[0]] = itemArrey[1]
            })
            this.deviceId = param.id
        }
        this.getDeviceDetail(this.deviceId)
    },
    methods: {
        getDeviceDetail: function(decviceId){
            console.log('id',decviceId)
            this.$http.post("/api/private/wash/ter/property/search", {
                id: decviceId,
            }).then(function(res){
                let result = res.json().data
                if(result) {
                    result.screenSize = `${result.screenHigh}*${result.screenWide}`
                    result.adExistDesc = result.adExist ? '支持':'不支持'
                    vm.device = result
                }
                console.log('vm.device: ', vm.device)
                
            })
        }
    }
})

$('.nav-each').removeClass('active');
$('.'+vm.pageName).addClass('active');