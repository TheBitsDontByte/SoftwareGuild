<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="en">

    <head>
        <meta charset="UTF-8">
        <title>Vend-o-Tron</title>
        <link href="css/bootstrap.css" rel="stylesheet"/>
        <style>
            @import url('https://fonts.googleapis.com/css?family=Orbitron');

            * {
                color: whitesmoke;
                font-family: "Orbitron";
            }

            body {

                background-image: url('http://wallpaper-gallery.net/images/high-resolution-blue-wallpaper/high-resolution-blue-wallpaper-4.jpg');
            }

            .items {
                border-radius: 10px;
                /*background-color: midnightblue;*/
                background-size: cover;
                background-color: darkblue;
                border: 3px solid whitesmoke;
                width: 28%;
                height: 0;
                padding-bottom: 25%;
                padding-top: 5px;
                margin: 2%;
                margin-top: 35px;
            }

            .items:hover {
                border-color: orange;
                color: orange;
            }

            .items:hover div, .items:hover span{
                border-color: orange;
                color: orange;
            }

            .btn:hover {
                color: whitesmoke;
                background-color: darkblue;
            }

            .item-name,
            .item-price {
                font-size: 20px;
                vertical-align: middle;
            }

            .selected-item {
                border-color: orange;
            }

            .display-borders {
                padding: 5px;
                min-height: 40px;
                border-radius: 10px;
            }
        </style>
    </head>

    <body>
        <!--SEPERATE EVERYTHING INTO GROUPS - HAVE GOOD NAMING FOR GROUPS-->
        <div class="container">

            <div class="row">
                <div class="col-md-12 text-center">
                    <div id="vending-machine-banner">
                        <h1>Vending Machine</h1>
                    </div>
                </div>
            </div>

            <hr>
            <!--TWO COLUMNS, ONE OF 9, ONE OF 3 BUT NOT ACTUAL ROW HERE ????-->

            <!--VENDING MACHINE STUFF DISPLAY-->
            <div class="row">
                <div class="col-md-8">

                    <div class="row" id="item-display">
                        <c:forEach var="item" items="${stock}">
                            <a id="item-template" href="selectItem?itemId=${item.itemId}">
                                <div class="col-md-4 items">
                                    <span class="item-number"><c:out value="${item.itemId}"/></span>
                                    <div class="row text-center item-name">
                                        <c:out value="${item.itemName}"/>
                                    </div>
                                    <br><br>
                                    <div class="row text-center item-price">
                                        <c:out value="$${item.itemCost}"/>
                                    </div>
                                    <br><br>
                                    <div class="row text-center item-quantity">
                                        Quantity Left: <c:out value="${item.itemQuantity}"/>
                                    </div>
                                </div>
                            </a>
                        </c:forEach>

                    </div>
                    <!--VENDING MACHINE BUTTONS / MENUS / ETC-->
                </div>
                <div class="col-md-4 text-center" style="border-left: 3px solid whitesmoke">
                    <div class="row">
                        <h2>Total Money In</h2>
                    </div>
                    <div class="row">
                        <div class="col-md-6 col-md-offset-3">
                            <h3 style="border: 2px solid whitesmoke;" id="current-money" class="display-borders">${currMoney}</h3>
                        </div>
                    </div>
                    <br>
                    <div class="row">
                        <div class="col-md-5 col-md-offset-1">
                            <a href="addMoney?value=dollar">
                                <button class="btn btn-block btn-default" id="add-dollar-btn">
                                    Add Dollar
                                </button>
                            </a>
                        </div>
                        <div class="col-md-5">
                            <a href="addMoney?value=quarter">
                                <button class="btn btn-block btn-default" id="add-quarter-btn" value="quarter">
                                    Add Quarter
                                </button>
                            </a>
                        </div>
                    </div>
                    <br>
                    <div class="row">
                        <div class="col-md-5 col-md-offset-1">
                            <a href="addMoney?value=dime">
                                <button class="btn btn-block btn-default" id="add-dime-btn" value="dime">
                                    Add Dime
                                </button>
                            </a>
                        </div>
                        <div class="col-md-5 ">
                            <a href="addMoney?value=nickel">
                                <button class="btn btn-block btn-default" id="add-nickel-btn" value="nickel">
                                    Add Nickel
                                </button>
                            </a>
                        </div>

                    </div>
                    <hr>
                    <div class="row">
                        <h2>Messages</h2>
                    </div>
                    <div class="row">
                        <div class="col-md-10 col-md-offset-1" id="display-message-box">
                            <h3 style="border: 2px solid whitesmoke" id="display-message" class="display-borders">
                                <c:out value="${message}"/>
                            </h3>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-3 col-md-offset-3" style="padding-top: 8px">
                            <h3>Item:</h3>
                        </div>
                        <div class="col-md-4">
                            <h3 style="border: 2px solid whitesmoke" id="display-item-num" class="display-borders">
                                <c:out value="${currItemId}"/>
                            </h3>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 col-md-offset-3">
                            <a href="makePurchase">
                                <button class="btn btn-block btn-default" id="purchase-btn">
                                    Purchase
                                </button>
                            </a>
                        </div>
                    </div>
                    <hr>
                    <div class="row">
                        <h3>Change</h3>
                    </div>
                    <div class="row">
                        <div class="col-md-12">
                            <h3 style="border: 2px solid whitesmoke; min-height: 70px;" class="display-borders" id="display-change">
                                <c:out value="${currChange}"/>
                            </h3>
                        </div>
                    </div>
                    <br>
                    <div class="col-md-6 col-md-offset-3">
                        <a href="makeChange"><button class="btn btn-block btn-default" id="change-btn" >
                                Make Change
                            </button>
                        </a>
                    </div>
                </div>
            </div>
        </div>

        <!--//Template for creating my stuff-->
        <a id="item-template" class="hidden">
            <div class="col-md-4 items">
                <span class="item-number">3</span>
                <div class="row text-center item-name">
                    Almond Joy
                </div>
                <br><br>
                <div class="row text-center item-price">
                    $ 1.25
                </div>
                <br><br>
                <div class="row text-center item-quantity">
                    Quantity Left: 10
                </div>
            </div>
        </a>

        <script src="js/jquery-3.1.1.min.js"></script>
        <script src="js/bootstrap.js"></script>
        <!--<script src="js/home.js"></script>-->
    </body>


</html>