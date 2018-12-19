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
        device: null,
        backupDevice: null,
        terOptions: null,
        terClientVersionOptions: [
            {
                label: 'Java',
                value: 'Java'
            },
            {
                label: 'Python',
                value: 'Python'
            },
        ],

        netMethodOptions: [
            {
                label: '有线宽带',
                value: 0
            },
            {
                label: '无线网卡',
                value: 1
            },
        ],

        terBusiModeOptions: [
            {
                label: '单网点无人值守',
                value: 0
            },
            {
                label: '单网点有人值守',
                value: 1
            },
            {
                label: '代理模式一',
                value: 2
            },
            {
                label: '代理模式二',
                value: 3
            }
        ],

        videoControlOptions: [
            {
                label: '海康威视',
                value: '海康威视'
            },
            {
                label: '乐橙',
                value: '乐橙'
            },
        ]

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

        // 设备详情
        getDeviceDetail: function(decviceId){
            console.log('id',decviceId)
            this.$http.post("/api/private/wash/ter/property/search", {
                id: decviceId,
            }).then(function(res){
                let result = res.json().data
                if(result) {
                    result.screenSize = `${result.screenHigh}*${result.screenWide}`
                    result.adExistDesc = result.adExist ? '支持':'不支持'
                    vm.device = [result]
                    let backupDevice = {}
                    Object.keys(result).forEach(function(item, index){
                        backupDevice[item] = {
                            value: result[item],
                            edit: false
                        }
                    })
                    vm.backupDevice = backupDevice
                }
                console.log('vm.backupDevice: ', vm.backupDevice)
                
            })
        },

        // 保存更改
        saveDevice: function(paramKey) {
            let updateDevice = {}
            let updatefield = {}
            console.log('保存更改vm.backupDevice: ', vm.backupDevice)
            if (paramKey == 'screen') {
                updatefield['highLimit'] = vm.backupDevice.highLimit.value
                updatefield['wideLimit'] = vm.backupDevice.wideLimit.value
            }else{
                updatefield[paramKey] = vm.backupDevice[paramKey].value
            }
            Object.keys(updatefield).forEach(function(item, index){
                updateDevice[item] = vm.backupDevice[item].value
            })
            updateDevice['id'] = vm.backupDevice.id.value
            console.log('待升级字段：', updateDevice, paramKey)
            this.updateDevice(updateDevice, paramKey)
        },

        // 取消
        cancel: function(paramKey) {
            vm.backupDevice[paramKey].edit = false
        },

        // 获取网点列表
        terQuerySearch: function(queryString, callback) {
            vm.backupDevice.terId.edit = true
            this.$http.post("/api/private/wash/ter/property/list/info", {
            }).then(function(res){
                let result = res.json().data.result
                if(result) {
                    result.forEach(function(item, index){
                        item['label'] = `${item.code}&${item.title}`
                        item['value'] = item.id.toString()
                    })
                    vm.terOptions = result
                }
            })
        },

        // 更改广告支持
        adExistChange: function() {
            let updateDevice = {}
            updateDevice['adExist'] = vm.backupDevice.adExist.value
            updateDevice['id'] = vm.backupDevice.id.value
            this.updateDevice(updateDevice, 'adExist')
        },

        // 屏幕编辑
        editScreen: function() {
            vm.backupDevice.screenHigh.edit = true
            vm.backupDevice.screenWide.edit = true
        },


        updateDevice: function(updateDevice, paramKey) {
            this.$http.post("/api/private/wash/ter/property/info/update", updateDevice).then(
                function(res){
                    let result = res.json()
                    if(result.code == 200) {
                        this.$message({
                            message: '保存成功',
                            type: 'success'
                        });
                        vm.getDeviceDetail(vm.deviceId)
                    } else {
                        this.$message.error(result.message);
                        // Object.keys(updatefield).forEach(function(item, index){
                        //     vm.device[0][item].value = vm.backupDevice[item]
                        // })
                        vm.backupDevice[paramKey].edit = false
                    }

                },
                function(res){
                    this.$message.error('保存失败');
                    // Object.keys(updatefield).forEach(function(item, index){
                    //     vm.device[0][item].value = vm.backupDevice[item]
                    // })
                    vm.backupDevice[paramKey].edit = false
                }
            )
        },
    }
})

$('.nav-each').removeClass('active');
$('.'+vm.pageName).addClass('active');


/* <el-autocomplete v-model="state4" :fetch-suggestions="querySearchAsync" placeholder="请输入内容" @select="handleSelect"></el-autocomplete>
data() {
    return {
      restaurants: [],
      state4: '',
      timeout:  nullnull
    };
  },
  methods: {
    loadAll() {
      return [
        { "value": "三全鲜食（北新泾店）", "address": "长宁区新渔路144号" },
        { "value": "Hot honey 首尔炸鸡（仙霞路）", "address": "上海市长宁区淞虹路661号" },
        { "value": "新旺角茶餐厅", "address": "上海市普陀区真北路988号创邑金沙谷6号楼113" },
        { "value": "泷千家(天山西路店)", "address": "天山西路438号" },
        { "value": "胖仙女纸杯蛋糕（上海凌空店）", "address": "上海市长宁区金钟路968号1幢18号楼一层商铺18-101" },
        { "value": "贡茶", "address": "上海市长宁区金钟路633号" },
        { "value": "豪大大香鸡排超级奶爸", "address": "上海市嘉定区曹安公路曹安路1685号" },
        { "value": "茶芝兰（奶茶，手抓饼）", "address": "上海市普陀区同普路1435号" },
        { "value": "十二泷町", "address": "上海市北翟路1444弄81号B幢-107" },
        { "value": "星移浓缩咖啡", "address": "上海市嘉定区新郁路817号" },
        { "value": "阿姨奶茶/豪大大", "address": "嘉定区曹安路1611号" },
        { "value": "新麦甜四季甜品炸鸡", "address": "嘉定区曹安公路2383弄55号" },
        { "value": "Monica摩托主题咖啡店", "address": "嘉定区江桥镇曹安公路2409号1F，2383弄62号1F" },
        { "value": "浮生若茶（凌空soho店）", "address": "上海长宁区金钟路968号9号楼地下一层" },
        { "value": "NONO JUICE  鲜榨果汁", "address": "上海市长宁区天山西路119号" },
        { "value": "CoCo都可(北新泾店）", "address": "上海市长宁区仙霞西路" },
        { "value": "快乐柠檬（神州智慧店）", "address": "上海市长宁区天山西路567号1层R117号店铺" },
        { "value": "Merci Paul cafe", "address": "上海市普陀区光复西路丹巴路28弄6号楼819" },
        { "value": "猫山王（西郊百联店）", "address": "上海市长宁区仙霞西路88号第一层G05-F01-1-306" },
        { "value": "枪会山", "address": "上海市普陀区棕榈路" },
        { "value": "纵食", "address": "元丰天山花园(东门) 双流路267号" },
        { "value": "钱记", "address": "上海市长宁区天山西路" },
        { "value": "壹杯加", "address": "上海市长宁区通协路" },
        { "value": "唦哇嘀咖", "address": "上海市长宁区新泾镇金钟路999号2幢（B幢）第01层第1-02A单元" },
        { "value": "爱茜茜里(西郊百联)", "address": "长宁区仙霞西路88号1305室" },
        { "value": "爱茜茜里(近铁广场)", "address": "上海市普陀区真北路818号近铁城市广场北区地下二楼N-B2-O2-C商铺" },
        { "value": "鲜果榨汁（金沙江路和美广店）", "address": "普陀区金沙江路2239号金沙和美广场B1-10-6" },
        { "value": "开心丽果（缤谷店）", "address": "上海市长宁区威宁路天山路341号" },
        { "value": "超级鸡车（丰庄路店）", "address": "上海市嘉定区丰庄路240号" },
        { "value": "妙生活果园（北新泾店）", "address": "长宁区新渔路144号" },
        { "value": "香宜度麻辣香锅", "address": "长宁区淞虹路148号" },
        { "value": "凡仔汉堡（老真北路店）", "address": "上海市普陀区老真北路160号" },
        { "value": "港式小铺", "address": "上海市长宁区金钟路968号15楼15-105室" },
        { "value": "蜀香源麻辣香锅（剑河路店）", "address": "剑河路443-1" },
        { "value": "北京饺子馆", "address": "长宁区北新泾街道天山西路490-1号" },
        { "value": "饭典*新简餐（凌空SOHO店）", "address": "上海市长宁区金钟路968号9号楼地下一层9-83室" },
        { "value": "焦耳·川式快餐（金钟路店）", "address": "上海市金钟路633号地下一层甲部" },
        { "value": "动力鸡车", "address": "长宁区仙霞西路299弄3号101B" },
        { "value": "浏阳蒸菜", "address": "天山西路430号" },
        { "value": "四海游龙（天山西路店）", "address": "上海市长宁区天山西路" },
        { "value": "樱花食堂（凌空店）", "address": "上海市长宁区金钟路968号15楼15-105室" },
        { "value": "壹分米客家传统调制米粉(天山店)", "address": "天山西路428号" },
        { "value": "福荣祥烧腊（平溪路店）", "address": "上海市长宁区协和路福泉路255弄57-73号" },
        { "value": "速记黄焖鸡米饭", "address": "上海市长宁区北新泾街道金钟路180号1层01号摊位" },
        { "value": "红辣椒麻辣烫", "address": "上海市长宁区天山西路492号" },
        { "value": "(小杨生煎)西郊百联餐厅", "address": "长宁区仙霞西路88号百联2楼" },
        { "value": "阳阳麻辣烫", "address": "天山西路389号" },
        { "value": "南拳妈妈龙虾盖浇饭", "address": "普陀区金沙江路1699号鑫乐惠美食广场A13" }
      ];
    },
    querySearchAsync(queryString, cb) {
      var restaurants = this.restaurants;
      var results = queryString ? restaurants.filter(this.createStateFilter(queryString)) : restaurants;

      clearTimeout(this.timeout);
      this.timeout = setTimeout(() => {
        cb(results);
      }, 3000 * Math.random());
    },

    createStateFilter(queryString) {
      return (state) => {
        return (state.value.toLowerCase().indexOf(queryString.toLowerCase()) === 0);
      };
    },

    handleSelect(item) {
      console.log(item);
    }
  },
  mounted() {
    this.restaurants = this.loadAll();
  }
}; */