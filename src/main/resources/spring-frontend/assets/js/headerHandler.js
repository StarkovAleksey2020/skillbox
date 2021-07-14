"use strict";

function testFunc(pointId) {
    const menuItemArray = document.querySelectorAll('.menu-item');
    menuItemArray.forEach(item => {
        item.classList.remove('menu-item_ACTIVE');
    })
    const itemActive = document.getElementById(pointId);
    localStorage.setItem('clickedPoint', pointId);
    itemActive.classList.add('menu-item_ACTIVE');
}

function setScrollElement(elementId) {
    localStorage.setItem('scrollElement', elementId);
}

