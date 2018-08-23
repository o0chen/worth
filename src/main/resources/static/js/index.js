var index = new Vue({
    el: '#index',
    data: {
        activeTabId: '1',
        tabs: [{
            id: '1',
            name: '首页',
            content: '首页'
        }],
        // 菜单
        menutrees: [],
    },
    mounted: function() {
        // 获取菜单列表
        getUserMenus();
    },
    methods: {
        handleOpen: function(key, keyPath) {
            console.log(key, keyPath);
        },
        handleClose: function(key, keyPath) {
            console.log(key, keyPath);
        },
        addTab: function(id, tabName, tabUrl) {
            // 打开tab，如果已经打开直接激活
            for (var i = 0; i < this.tabs.length; i++) {
                tab = this.tabs[i];
                if (tab.id == id) {
                    this.activeTabId = id;
                    return;
                }
            }
            this.tabs.push({
                id: id,
                name: tabName,
                content: ''
            });
            this.activeTabId = id;
            getTemplate(id, tabUrl);
        },
        removeTab: function(id) {
            if (id === '1') {
                return;
            }
            var _tabs = this.tabs;
            var _activeTabId = this.activeTabId;
            if (_activeTabId === id) {
                for (var i = 0; i < _tabs.length; i++) {
                    var tab = _tabs[i];
                    if (tab.id === id) {
                        var nextTab = _tabs[i + 1] || _tabs[i - 1];
                        if (nextTab) {
                            _activeTabId = nextTab.id;
                        }
                    }
                }
            }

            this.activeTabId = _activeTabId;
            this.tabs = []
            for (var i = 0; i < _tabs.length; i++) {
                tab = _tabs[i];
                if (tab.id !== id) {
                    this.tabs.push(tab);
                }
            }
        },
    }
});

function getTemplate(id, url) {

    $.ajax({
        url: url,
        async: true,
        success: function(data) {
//					console.log(data);
            if (data.code == 3) {
                error("登陆超时");
            } else {
                $('#'+id).html(data);
            }

        },
        error: function(e) {
            $('#'+id).html("网络异常");
        }
    })
}

function getUserMenus() {
    var url = contentPath + 'user-menus';
    ajax(url, null, 'GET', {}).then(function(data) {
        log(data);
        index.menutrees = data.data;
    })
}