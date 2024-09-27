//const price = document.getElementsByClassName("priceIn");
//Array.from(price).forEach(element => {
//    let priceStr = element.textContent;
//    
//    // Check if the price string is valid for formatting
//    if (priceStr.length >= 4) {
//        // Reverse the string to insert commas every three digits
//        let newArr = priceStr.split("").reverse();
//        
//        // Loop through the array to insert a comma every three digits
//        for (let i = 3; i < newArr.length; i += 4) {
//            newArr.splice(i, 0, ",");
//        }
//
//        // Reverse the array back and join it into a string
//        priceStr = newArr.reverse().join("");
//        
//        // Update the element's textContent with the formatted price
//        element.textContent = priceStr;
//    }
//});

//! view Admil Login Page
//const admin = document.querySelector(".adminSelect");
//const closeLogin = document.querySelector(".ri-close-large-line");
//const loginPage = document.querySelector(".adminLogin");
//
//admin.addEventListener("click",()=>{
//    loginPage.style.display = "block";
//})
//closeLogin.addEventListener("click",()=>{
//    loginPage.style.display = "none";
//}) 