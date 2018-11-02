/**
 * @Author: jinghy
 * @Date: 2018/2/24/024 14:50
 */
app.controller("goodlist",function($scope,$rootScope, $http, $timeout, $ionicScrollDelegate,  $stateParams, $ionicLoading,$ionicSlideBoxDelegate){
    $scope.paginfo={pageNum:1,pageSize:10 ,coleam:"name"  };
    $scope.goodlist=[];

    $scope.myKeyup = function(e){
        var keycode = window.event?e.keyCode:e.which;
        if(keycode==13){
            $scope.paginfo.colval= $("#goodlist_search").val();
            $scope.initData();
        }
    };

    $scope.initData=function(isadd){

        $ionicLoading.show();
        $scope.paginfo.token=$rootScope.getToken();
        $http({url:"/goodlist.do",method:"post", params: $scope.paginfo}).success(function (data) {
            $scope.paginfo.pageNum= data.pageNum;
            $scope.paginfo.total=data.total;//总记录
            $scope.goodlist=data.rows;
          //  $scope.initTemp(data.rows,isadd );
            $ionicLoading.hide();
        });
    }
    $scope.initTemp=function(data,isadd){
        var dttalis=[];
        angular.forEach(data, function (item) {
            dttalis.push(
                ['<ion-item class="item item-thumbnail-left" href="#/gooddeatil/',item.id,'"><img src="',$rootScope.imgsv+item.img,'" class="imgLost"> <h2>',item.name,'<span class="fr sm">',$rootScope.gd_leve[item.grade]    +"   "+ item.price+'元/'+$rootScope.gd_unit[item.unit],'</span></h2>' ,
                    ' <p class="color-99" style="font-size: 13px;"><i class="iconfont icon-techreport-"></i>',item.compname,'</p>' ,
                    '<p style="font-size: 13px;">',item.addtime,'</p>' ,
                    '</ion-item>'].join("")
            );
        });
        if(isadd){
            $("#ionc_goodlist").append(dttalis.join(""));
        }else{
            $("#ionc_goodlist").html(dttalis.join(""));
        }
  }



    $scope.can_loadMore=function(flag){
        if(!$scope.paginfo.total){ return true;}
        return  $scope.goodlist.length<$scope.paginfo.total ;
    }
    $scope.loadMore=function(){
        $scope.paginfo.pageNum++;
        $scope.initData();
        $scope.$broadcast("scroll.infiniteScrollComplete");
    }
    $scope.initData();
});