/**
 * Created by suzhao on 2017/3/16.
 */


// 使用方法：
// 页面头部添加 <meta name="viewport" content="width=device-width, initial-scale=1.0">
// 在页面中引入jquery (为了处理PC端的显示)，引入该文件，
// 按照视网膜@2 的视觉稿，sublime插件中rem基准单位转换为37.5（视觉稿按照IPHONE6的尺寸，宽度750）

;(function(win, lib) {
    var doc = win.document;
    var docEl = doc.documentElement;
    var tid;
    var flexible = lib.flexible || (lib.flexible = {});

    function refreshRem() {
        var width = docEl.getBoundingClientRect().width;

        // 对于PC端的显示做处理
        // if(width > 540){
            // width = 540;
            // var html = document.getElementsByTagName('html');
            // html.style.width = '540px';
            // html.style.margin = '0 auto';

            // $('html').css('margin', '0 auto').css('width', '540px');
        // }
        // PC端显示处理结束


        var rem = width / 10;
        // 大于500不做判断，移动PC通用
        // if (width > 540) {
        //     return;
        // } else {
            // 检测UC浏览器进行Hack
            if ((/UCBrowser/i).test(win.navigator.userAgent)) {
                var styleTag = doc.createElement('style');
                var styleCont = doc.createTextNode('html{font-size: ' + rem + 'px !important;}');
                var titleTag = doc.getElementsByTagName('title')[0];
                styleTag.appendChild(styleCont);
                titleTag.parentNode.insertBefore(styleTag, titleTag);
            } else {
                docEl.style.fontSize = rem + 'px';
            }
        // }
        // 全局暴露Rem值
        flexible.rem = win.rem = rem;
    }

    // Resize时触发刷新Rem值
    win.addEventListener('resize', function() {
        clearTimeout(tid);
        tid = setTimeout(refreshRem, 300);
    }, false);
    win.addEventListener('pageshow', function(e) {
        if (e.persisted) {
            clearTimeout(tid);
            tid = setTimeout(refreshRem, 300);
        }
    }, false);

    // 初始化及赋给全局
    refreshRem();
    flexible.refreshRem = refreshRem;

    // 转化Rem值到PX
    flexible.rem2px = function(d) {
        var val = parseFloat(d) * this.rem;
        if (typeof d === 'string' && d.match(/rem$/)) {
            val += 'px';
        }
        return val;
    };

    // 转换PX值到Rem
    flexible.px2rem = function(d) {
        var val = parseFloat(d) / this.rem;
        if (typeof d === 'string' && d.match(/px$/)) {
            val += 'rem';
        }
        return val;
    };

    // 获取页面宽度
    flexible.isMobile = function() {
        return docEl.getBoundingClientRect().width < 540 ? true : false;
    };

    // 获取网页参数值
    win.getParams = function(arg) {
        var addr = win.location.search,
            paramsObj = {};
        if (addr.indexOf('?') != -1) {
            var paramsArr = addr.substr(1).split('&');
            for (var i = 0; i < paramsArr.length; i++) {
                paramsObj[paramsArr[i].split('=')[0]] = decodeURIComponent(paramsArr[i].split('=')[1]);
            }
        }
        return paramsObj[arg];
    };

})(window, window['lib'] || (window['lib'] = {}));