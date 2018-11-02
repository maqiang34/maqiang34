//程序开始
var host = "https://lianku.org.cn/";
var app = angular.module('ionicApp', ['ionic', "oc.lazyLoad"]);
app.config(["$provide", "$compileProvider", "$controllerProvider", "$filterProvider", function ($provide, $compileProvider, $controllerProvider, $filterProvider) {
    app.controller = $controllerProvider.register;
    app.directive = $compileProvider.directive;
    app.filter = $filterProvider.register;
    app.factory = $provide.factory;
    app.service = $provide.service;
    app.constant = $provide.constant;
}]);
app.config(['$httpProvider', function($httpProvider) {
    $httpProvider.defaults.withCredentials = true;
}])
app.run(['$rootScope', '$http', '$timeout', '$interval', '$ionicHistory', '$ionicTabsDelegate', '$ionicScrollDelegate', '$ionicLoading', '$ionicPopup', '$state', '$ionicPlatform',
    function ($rootScope, $http, $timeout, $interval, $ionicHistory, $ionicTabsDelegate, $ionicScrollDelegate, $ionicLoading, $ionicPopup, $state, $ionicPlatform) {
        $http.defaults.withCredentials = true;
        $http.defaults.headers = {'Content-Type': 'application/x-www-form-urlencoded'};
        $rootScope.getQueryString=function(name) {
            var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
            var r = window.location.search.substr(1).match(reg);
            if (r != null) return unescape(r[2]);
            return null;
        };
        /*全局alert更改*/
        window.alert = function (message) {
            var alertPopup = $ionicPopup.alert({
                title: '温馨提示',
                template: message
            });
        };
        $rootScope.onSwipeRight = function () {//向右滑动
            $ionicHistory.goBack();
        };
        $rootScope.onDragRight = function () {//向右拖拽
            $ionicHistory.goBack();
        }

    }]);

app.config(function ($stateProvider, $urlRouterProvider, $ionicConfigProvider) {
    // $ionicConfigProvider.views.maxCache(1);
    var version = '?v=3.6.0';
    $stateProvider
    //1.首页========================================================================================================================================
        .state('home', {
            url: "/home",
            views: {
                'home-tab': {
                    controller: 'home',
                    templateUrl: "app/com/templates/home.html"
                    , resolve: {
                        deps: ["$ocLazyLoad", function ($ocLazyLoad) {
                            return $ocLazyLoad.load("app/com/js/home.js" + version);
                        }]
                    }
                }
            }
        })
        //2.商品资源================================================
        .state('goodlist', {
            url: "/goodlist",
            cache: true,
            views: {
                'goodlist-tab': {
                    controller: 'goodlist',
                    templateUrl: "app/com/templates/goodlist.html"
                    , resolve: {
                        deps: ["$ocLazyLoad", function ($ocLazyLoad) {
                            return $ocLazyLoad.load("app/com/js/goodlist.js" + version);
                        }]
                    }
                }
            }
        })
        //商品信息
        .state('gooddeatil', {
            url: "/gooddeatil/{code}",
            cache: true,
            // params: {"keyword": null, "istemperaturestandard": null},
            views: {
                'goodlist-tab': {
                    controller: 'gooddeatil',
                    templateUrl: "app/com/templates/gooddeatil.html"
                    , resolve: {
                        deps: ["$ocLazyLoad", function ($ocLazyLoad) {
                            return $ocLazyLoad.load("app/com/js/gooddeatil.js" + version);
                        }]
                    }
                }
            }
        })
        .state('user', {
            url: "/user",
            //cache: false,
            views: {
                'user-tab': {
                    controller: 'user',
                    templateUrl: "app/com/templates/user.html"
                    , resolve: {
                        deps: ["$ocLazyLoad", function ($ocLazyLoad) {
                            return $ocLazyLoad.load("app/com/js/user.js" + version);
                        }]
                    }
                }
            }
        });

    $urlRouterProvider.otherwise("/home");
});

