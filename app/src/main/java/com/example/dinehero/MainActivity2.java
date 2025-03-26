package com.example.dinehero;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dinehero.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity {

    private RecyclerView products;
    private static ArrayList<Product> productsList = new ArrayList<>();

    private static ArrayList<Product> searchArrayList = new ArrayList<>();

    private BottomNavigationView TNV;

    TextView noResults;

    private EditText search;



     ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!ForYouActivity.alreadyPlants) {
            ForYouActivity.plantList.add(new ForYouActivity.Plant("Wheat", "golden", "green", "yellow", "brown", "wind", 48, "grass-like", "wind-pollinated, grows well in temperate regions"));
            ForYouActivity.plantList.add(new ForYouActivity.Plant("Corn", "yellow", "green", "yellow", "brown", "wind", 72, "tall stalk", "wind-pollinated, requires warm weather"));
            ForYouActivity.plantList.add(new ForYouActivity.Plant("Soybean", "green", "green", "purple", "brown", "bees", 36, "bushy", "fixes nitrogen, improves soil fertility"));
            ForYouActivity.plantList.add(new ForYouActivity.Plant("Rice", "golden", "green", "white", "brown", "wind", 36, "grass-like", "requires flooded fields, wind-pollinated"));
            ForYouActivity.plantList.add(new ForYouActivity.Plant("Barley", "golden", "green", "yellow", "brown", "wind", 40, "grass-like", "fast-growing, drought-resistant"));
            ForYouActivity.plantList.add(new ForYouActivity.Plant("Oats", "golden", "green", "yellow", "brown", "wind", 42, "grass-like", "high yield, thrives in cooler climates"));
            ForYouActivity.plantList.add(new ForYouActivity.Plant("Sorghum", "red", "green", "yellow", "brown", "wind", 60, "tall stalk", "drought-resistant, used for grain and forage"));
            ForYouActivity.plantList.add(new ForYouActivity.Plant("Millet", "yellow", "green", "yellow", "brown", "wind", 50, "grass-like", "heat-tolerant, used for grains and fodder"));
            ForYouActivity.plantList.add(new ForYouActivity.Plant("Potato", "brown", "green", "purple", "brown", "bees", 24, "underground tuber", "high-yield root crop"));
            ForYouActivity.plantList.add(new ForYouActivity.Plant("Sweet Potato", "orange", "green", "purple", "brown", "bees", 18, "vine", "nutrient-dense root crop"));
            ForYouActivity.plantList.add(new ForYouActivity.Plant("Carrot", "orange", "green", "white", "brown", "bees", 12, "taproot", "rich in beta-carotene, prefers loose soil"));
            ForYouActivity.plantList.add(new ForYouActivity.Plant("Beet", "red", "green", "yellow", "brown", "bees", 12, "bulb", "high in iron, used for sugar production"));
            ForYouActivity.plantList.add(new ForYouActivity.Plant("Onion", "white", "green", "white", "brown", "bees", 18, "bulb", "strong aroma deters pests, stores well"));
            ForYouActivity.plantList.add(new ForYouActivity.Plant("Garlic", "white", "green", "white", "brown", "bees", 18, "bulb", "natural pest repellent, high market demand"));
            ForYouActivity.plantList.add(new ForYouActivity.Plant("Tomato", "red", "green", "yellow", "brown", "bees", 24, "bushy", "high-value crop, needs staking for support"));
            ForYouActivity.plantList.add(new ForYouActivity.Plant("Pepper", "red", "green", "white", "brown", "bees", 24, "bushy", "thrives in warm temperatures"));
            ForYouActivity.plantList.add(new ForYouActivity.Plant("Cabbage", "green", "green", "yellow", "brown", "bees", 18, "compact leaves", "cold-tolerant, high market demand"));
            ForYouActivity.plantList.add(new ForYouActivity.Plant("Lettuce", "green", "green", "yellow", "brown", "wind", 12, "leafy", "fast-growing, prefers cooler temperatures"));
            ForYouActivity.plantList.add(new ForYouActivity.Plant("Spinach", "green", "green", "yellow", "brown", "wind", 12, "leafy", "nutrient-rich, thrives in early spring"));
            ForYouActivity.plantList.add(new ForYouActivity.Plant("Broccoli", "green", "green", "yellow", "brown", "bees", 24, "tall stalk", "high-value crop, cool-weather plant"));
            ForYouActivity.plantList.add(new ForYouActivity.Plant("Cauliflower", "white", "green", "yellow", "brown", "bees", 24, "tall stalk", "requires rich soil, sensitive to heat"));
            ForYouActivity.plantList.add(new ForYouActivity.Plant("Pumpkin", "orange", "green", "yellow", "brown", "bees", 24, "vine", "long-growing season, attracts pollinators"));
            ForYouActivity.plantList.add(new ForYouActivity.Plant("Watermelon", "red", "green", "yellow", "brown", "bees", 18, "vine", "high-water crop, needs warm temperatures"));
            ForYouActivity.plantList.add(new ForYouActivity.Plant("Cucumber", "green", "green", "yellow", "brown", "bees", 18, "vine", "climbing plant, requires trellising"));
            ForYouActivity.plantList.add(new ForYouActivity.Plant("Zucchini", "green", "green", "yellow", "brown", "bees", 18, "bushy", "high-yield, short growing period"));
            ForYouActivity.plantList.add(new ForYouActivity.Plant("Strawberry", "red", "green", "white", "brown", "bees", 12, "low-growing", "high-value fruit, requires mulching"));
            ForYouActivity.plantList.add(new ForYouActivity.Plant("Blueberry", "blue", "green", "white", "red", "bees", 36, "shrub", "acidic soil-loving, attracts pollinators"));
            ForYouActivity.plantList.add(new ForYouActivity.Plant("Raspberry", "red", "green", "white", "brown", "bees", 48, "shrub", "prone to spreading, needs support"));
            ForYouActivity.plantList.add(new ForYouActivity.Plant("Apple", "red", "green", "pink", "brown", "bees", 144, "tree", "requires cross-pollination for best yield"));
            ForYouActivity.plantList.add(new ForYouActivity.Plant("Pear", "green", "green", "white", "brown", "bees", 180, "tree", "thrives in well-drained soil"));
            ForYouActivity.plantList.add(new ForYouActivity.Plant("Peach", "orange", "green", "pink", "brown", "bees", 180, "tree", "early blooming, sensitive to frost"));
            ForYouActivity.plantList.add(new ForYouActivity.Plant("Grapes", "purple", "green", "white", "brown", "bees", 72, "vine", "requires pruning, grows well in warm climates"));
            ForYouActivity.plantList.add(new ForYouActivity.Plant("Alfalfa", "green", "green", "purple", "brown", "bees", 36, "legume", "fixes nitrogen, great forage crop"));
            ForYouActivity.plantList.add(new ForYouActivity.Plant("Clover", "green", "green", "pink", "brown", "bees", 12, "ground cover", "improves soil fertility, attracts bees"));
            ForYouActivity.plantList.add(new ForYouActivity.Plant("Sunflower", "yellow", "green", "yellow", "brown", "bees", 60, "tall stalk", "high oil content, attracts pollinators"));
            ForYouActivity.plantList.add(new ForYouActivity.Plant("Cotton", "white", "green", "yellow", "brown", "bees", 48, "shrub", "warm-season crop, used for textiles"));

            ForYouActivity.alreadyPlants = true;
        }


        products = findViewById(R.id.Products);

        TNV = findViewById(R.id.topNavView);
        TNV.setItemActiveIndicatorEnabled(false);

        search = findViewById(R.id.searchET);
        noResults = findViewById(R.id.noResults);
        noResults.setVisibility(View.INVISIBLE);




        TNV.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == findViewById(R.id.btnDiscover).getId()) {
                    openCalenderActivity();

                } else if (item.getItemId() == findViewById(R.id.btnForYou).getId()) {
                    openForYouActivity();
                } else if (item.getItemId() == findViewById(R.id.btnFollowing).getId()) {
                    openAIChatActivity();//openProfileActivity();
                }
                else if (item.getItemId() == findViewById(R.id.btnSearch).getId()) {
                    openMainActivity();
                }

                return false;
            }
        });










        if(ForYouActivity.getFirst()) {



            productsList.add(new Product("400lb of Carrots", "Walmart is a global retail giant known for its low prices and wide selection of products. Founded in 1962 by Sam Walton, it operates thousands of stores worldwide, including supercenters, discount stores, and neighborhood markets. Walmart offers groceries, electronics, clothing, home goods, and more. With a strong emphasis on affordability and convenience, it also provides online shopping and services like pharmacy and financial solutions. Its mission is to help customers Save Money. Live Better.", "Walmart", R.drawable.picnic,false,"07/05/24","405-567-9847","walmart.com","2200 W Danforth Rd, Edmond",480.24));
            productsList.add(new Product("200lb of Potatoes", "Sam’s Club is a membership-based warehouse retailer that provides bulk groceries, fresh produce, and household essentials at competitive prices. Catering to individuals and businesses alike, it offers an extensive range of food products, meats, and pantry staples. With a strong emphasis on cost savings, Sam’s Club also provides online shopping, curbside pickup, and home delivery services, ensuring convenience for its customers while maintaining its commitment to affordability and quality.",
                    "Sam’s Club", R.drawable.picnic, true, "07/10/24", "405-342-6789", "samsclub.com", "1900 Memorial Rd, Oklahoma City", 220.50));

            productsList.add(new Product("150lb of Tomatoes", "Whole Foods Market is a popular grocery chain specializing in organic and natural foods, free from artificial preservatives, flavors, and colors. With a commitment to sustainability, it offers farm-fresh fruits, vegetables, responsibly sourced meats, and organic dairy. Customers can shop for high-quality, health-conscious products in-store or online. Whole Foods partners with local farms, ensuring fresh produce while supporting sustainable agriculture and ethical food production.",
                    "Whole Foods Market", R.drawable.picnic, false, "07/12/24", "405-789-6543", "wholefoods.com", "6001 N Western Ave, Oklahoma City", 180.75));

            productsList.add(new Product("300lb of Onions", "Crest Foods is a family-owned supermarket chain that has been serving Oklahoma communities since 1946. Known for its competitive prices, fresh produce, and quality meats, Crest Foods offers customers a wide selection of groceries, household essentials, and bulk goods. Shoppers appreciate the friendly service, weekly discounts, and locally sourced products. Whether shopping for a small meal or a large gathering, Crest Foods provides reliable quality and affordability.",
                    "Crest Foods", R.drawable.picnic, false, "07/08/24", "405-234-9876", "crestfoodsok.com", "4503 NW 23rd St, Oklahoma City", 275.30));

            productsList.add(new Product("500lb of Apples", "Sprouts Farmers Market focuses on fresh, organic produce, bulk foods, and natural grocery products. It caters to health-conscious shoppers with an array of farm-fresh fruits, vegetables, sustainable meats, and wholesome snacks. Sprouts is dedicated to making natural and organic foods more accessible and affordable. Whether shopping in-store or online, customers can find high-quality, fresh ingredients that support a healthy lifestyle, backed by a commitment to sustainability.",
                    "Sprouts Farmers Market", R.drawable.picnic, false, "07/15/24", "405-765-4321", "sprouts.com", "12200 N May Ave, Oklahoma City", 600.00));

            productsList.add(new Product("250lb of Lettuce", "The Urban Farm Stand is a dedicated marketplace connecting local farmers directly with consumers, providing fresh, seasonal produce, dairy, and organic goods. Supporting sustainable agriculture, the farm stand ensures customers receive high-quality farm-to-table produce while promoting environmentally responsible practices. With a focus on freshness and quality, The Urban Farm Stand offers a reliable source for organic fruits, vegetables, and farm goods straight from local growers.",
                    "The Urban Farm Stand", R.drawable.picnic, false, "07/14/24", "405-888-7654", "urbanfarmstand.com", "321 E Main St, Norman", 310.50));

            productsList.add(new Product("100lb of Green Beans", "BuyFresh Co. specializes in bridging the gap between local farmers and buyers, offering fresh produce to restaurants, stores, and consumers. By sourcing seasonal vegetables and fruits directly from farms, BuyFresh Co. guarantees freshness, quality, and farm-to-table integrity. The company is committed to supporting small-scale agriculture and ensuring customers receive high-quality, locally grown food at fair prices, promoting sustainable farming and community-supported agriculture.",
                    "BuyFresh Co.", R.drawable.picnic, false, "07/18/24", "405-555-6543", "buyfresh.com", "745 S Broadway, Edmond", 95.00));

            productsList.add(new Product("350lb of Bell Peppers", "RedFork Diner is a locally owned, farm-to-table restaurant that prioritizes fresh, locally sourced ingredients. With a menu that changes seasonally, the diner ensures the best flavors by using high-quality farm produce. RedFork partners with nearby farms to bring sustainable, organic food to the community, offering a fresh and delicious dining experience. It’s a favorite for those who appreciate homemade, natural, and farm-fresh meals.",
                    "RedFork Diner", R.drawable.picnic, false, "07/20/24", "405-234-7890", "redforkdiner.com", "1101 N Classen Blvd, Oklahoma City", 420.00));

            productsList.add(new Product("275lb of Sweet Corn", "Horizon Farms Market is a family-run business that prides itself on delivering farm-fresh produce, dairy, and meats. Committed to sustainability and ethical farming, Horizon Farms Market offers customers high-quality, naturally grown food straight from local farms. The market focuses on organic produce, pasture-raised meats, and minimally processed products, ensuring a healthier option for shoppers who value quality and environmental responsibility.",
                    "Horizon Farms Market", R.drawable.picnic, false, "07/22/24", "405-678-1234", "horizonfarms.com", "7802 SE 15th St, Midwest City", 345.60));

            productsList.add(new Product("120lb of Strawberries", "Berry Good Café is a cozy, health-focused eatery that sources fresh, organic ingredients directly from local farms. Specializing in farm-fresh smoothies, baked goods, and seasonal dishes, Berry Good Café is committed to sustainability and quality. The café supports small-scale farmers by using organic fruits, vegetables, and natural ingredients, making it a go-to spot for fresh and nutritious meals with locally sourced goodness.",
                    "Berry Good Café", R.drawable.picnic, false, "07/25/24", "405-999-3210", "berrygoodcafe.com", "95 N Broadway, Moore", 150.00));

            productsList.add(new Product("450lb of Cabbage", "GreenHarvest Grocers is a neighborhood grocery store focused on farm-direct, organic produce. By working closely with local farmers, GreenHarvest ensures customers receive the freshest vegetables, fruits, and dairy products available. With a mission to provide high-quality, natural foods at fair prices, GreenHarvest Grocers is committed to sustainability, healthy eating, and supporting local agriculture while offering a wide variety of farm-fresh selections.",
                    "GreenHarvest Grocers", R.drawable.picnic, false, "07/27/24", "405-112-3344", "greenharvestgrocers.com", "233 SW 4th St, Yukon", 390.25));
            productsList.add(new Product("300lb of Zucchini", "Farmer’s Pride Market is a locally owned grocery store specializing in fresh, organic, and non-GMO produce. Partnering with local farmers, the market ensures customers have access to the highest quality fruits, vegetables, dairy, and meats. The store is dedicated to sustainable farming, ethical sourcing, and supporting small-scale agricultural businesses. Whether shopping for a weekly meal or stocking up on fresh ingredients, Farmer’s Pride Market is a go-to destination for farm-to-table products.",
                    "Farmer’s Pride Market", R.drawable.picnic, false, "07/29/24", "405-321-7894", "farmerspridemarket.com", "412 NE 2nd St, Norman", 350.75));

            productsList.add(new Product("400lb of Oranges", "Sunshine Grocers is a family-run supermarket that focuses on fresh, farm-direct produce. Offering an extensive selection of organic fruits, vegetables, dairy, and pantry essentials, it is committed to supporting local farmers and sustainable agriculture. Sunshine Grocers ensures every item is carefully sourced to maintain quality and freshness. With in-store shopping, home delivery, and curbside pickup, customers can easily access high-quality produce at fair prices while enjoying a friendly, neighborhood shopping experience.",
                    "Sunshine Grocers", R.drawable.picnic, false, "07/30/24", "405-678-9987", "sunshinegrocers.com", "901 S Broadway, Moore", 420.60));

            productsList.add(new Product("250lb of Cucumbers", "Harvest Table Bistro is a farm-to-table restaurant that takes pride in serving fresh, locally sourced ingredients. With an ever-changing seasonal menu, the bistro ensures top-quality meals crafted with the finest produce, meats, and dairy. By working directly with local farms, Harvest Table Bistro provides nutritious, flavorful dishes while supporting sustainable agriculture. Whether stopping by for breakfast, lunch, or dinner, guests can enjoy wholesome, farm-fresh meals prepared with care and expertise.",
                    "Harvest Table Bistro", R.drawable.picnic, false, "08/01/24", "405-234-6578", "harvesttablebistro.com", "2300 N Penn Ave, Oklahoma City", 310.45));

            productsList.add(new Product("500lb of Watermelon", "Fresh Picked Farms is a farm co-op dedicated to providing locally grown produce to supermarkets, restaurants, and direct consumers. By sourcing fruits and vegetables straight from regional farmers, it guarantees peak freshness, superior taste, and high nutritional value. Customers can order seasonal produce boxes, bulk deliveries, or shop at partner markets. Fresh Picked Farms emphasizes ethical farming, sustainability, and fair pricing, ensuring access to high-quality, farm-fresh foods for all.",
                    "Fresh Picked Farms", R.drawable.picnic, false, "08/03/24", "405-654-8877", "freshpickedfarms.com", "1050 W Main St, Edmond", 520.80));

            productsList.add(new Product("350lb of Spinach", "GreenFork Café is a locally owned eatery that specializes in fresh, organic, and locally sourced meals. With a focus on farm-to-table ingredients, the café offers vibrant salads, wholesome bowls, and hearty sandwiches made with seasonal produce. Every dish is crafted using the best ingredients from local farmers, ensuring exceptional taste and nutritional value. GreenFork Café is committed to sustainability and healthy eating, making it a top choice for those who value quality food.",
                    "GreenFork Café", R.drawable.picnic, false, "08/05/24", "405-333-2256", "greenforkcafe.com", "150 E Sheridan Ave, Oklahoma City", 285.30));

            productsList.add(new Product("275lb of Kale", "Nature’s Basket Market is a health-focused grocery store offering a diverse selection of organic produce, fresh dairy, and sustainable meats. Partnering with local farms, it guarantees high-quality, farm-fresh goods that promote healthy eating. The store provides in-store shopping, online ordering, and home delivery services. Nature’s Basket Market is dedicated to affordability, environmental responsibility, and supporting small-scale agriculture while ensuring customers receive the freshest, most nutritious ingredients.",
                    "Nature’s Basket Market", R.drawable.picnic, false, "08/07/24", "405-567-9998", "naturesbasketmarket.com", "980 SE 29th St, Midwest City", 395.20));

            productsList.add(new Product("400lb of Carrots", "Golden Harvest Deli is a farm-fresh eatery that specializes in organic salads, sandwiches, and soups made with locally sourced ingredients. The deli prides itself on using high-quality, farm-to-table produce to create healthy, flavorful meals. By supporting regional farmers, Golden Harvest ensures sustainability and freshness in every bite. Whether dining in or taking out, customers can enjoy nutritious, handcrafted meals prepared with care and expertise, making it a favorite for fresh and healthy eating.",
                    "Golden Harvest Deli", R.drawable.picnic, false, "08/09/24", "405-789-6541", "goldenharvestdeli.com", "650 N Walker Ave, Oklahoma City", 350.40));

            productsList.add(new Product("325lb of Bell Peppers", "Pure Harvest Supermarket is a full-service grocery store that specializes in farm-fresh produce, organic products, and sustainably sourced meats. Working directly with local farms, the supermarket ensures high-quality goods at affordable prices. Customers appreciate the store’s commitment to freshness, environmental sustainability, and community support. With a variety of natural food products and an easy shopping experience, Pure Harvest Supermarket is a top choice for health-conscious individuals and families.",
                    "Pure Harvest Supermarket", R.drawable.picnic, false, "08/11/24", "405-222-5678", "pureharvestsupermarket.com", "411 NE 10th St, Oklahoma City", 375.85));

            productsList.add(new Product("450lb of Bananas", "The Local Pantry is a specialty grocery store that connects small farmers with urban consumers. Offering a wide selection of fresh produce, dairy, and baked goods, The Local Pantry prioritizes quality and sustainability. Shoppers can find seasonal fruits and vegetables at peak freshness, along with ethically sourced meats and artisanal products. By emphasizing farm-to-table shopping, The Local Pantry supports local agriculture while providing customers with high-quality, organic groceries.",
                    "The Local Pantry", R.drawable.picnic, false, "08/13/24", "405-121-7765", "thelocalpantry.com", "220 W Main St, Norman", 410.75));

            productsList.add(new Product("500lb of Cabbage", "Blue Sky Diner is a farm-to-table restaurant that sources fresh ingredients from regional farms. With a diverse menu featuring seasonal produce, pasture-raised meats, and homemade baked goods, it provides a unique dining experience centered around fresh and natural flavors. Blue Sky Diner is committed to sustainability, supporting local farmers, and reducing food waste. Guests can enjoy delicious, home-cooked meals made with the freshest ingredients in a welcoming and relaxed atmosphere.",
                    "Blue Sky Diner", R.drawable.picnic, false, "08/15/24", "405-897-5643", "blueskydiner.com", "1750 N Lincoln Blvd, Oklahoma City", 495.90));
            productsList.add(new Product("350lb of Tomatoes", "Farm Fresh Grocers is a community-focused supermarket that prioritizes locally sourced, high-quality produce. With direct partnerships with regional farms, it ensures that customers receive the freshest vegetables, fruits, dairy, and meats at affordable prices. The store also features a selection of organic and non-GMO products. Offering both in-store and online shopping, Farm Fresh Grocers provides a convenient, sustainable, and healthy way to shop while supporting local agriculture.",
                    "Farm Fresh Grocers", R.drawable.picnic, false, "08/17/24", "405-987-6543", "farmfreshgrocers.com", "900 NW 5th St, Moore", 365.40));

            productsList.add(new Product("275lb of Strawberries", "Berry Bliss Café is a farm-to-table restaurant specializing in fresh, organic, and seasonal ingredients. Featuring locally grown fruits and vegetables, it offers smoothies, breakfast bowls, and healthy snacks that highlight the natural sweetness of farm-fresh produce. With a strong commitment to sustainability and nutrition, Berry Bliss Café sources directly from nearby farms, ensuring peak freshness. Whether stopping in for a quick bite or a refreshing drink, customers always enjoy wholesome, flavorful meals.",
                    "Berry Bliss Café", R.drawable.picnic, false, "08/19/24", "405-765-4321", "berryblisscafe.com", "760 E 2nd St, Norman", 290.30));

            productsList.add(new Product("500lb of Potatoes", "Sun Valley Supermarket is a leading grocery store dedicated to offering fresh, farm-sourced products. Customers can find a wide variety of vegetables, fruits, dairy, and organic options, all sourced from trusted local farms. Sun Valley Supermarket is known for its high standards in quality and sustainability. Whether shopping for daily essentials or specialty items, customers appreciate the market’s focus on affordability and freshness, making it a go-to destination for health-conscious shoppers.",
                    "Sun Valley Supermarket", R.drawable.picnic, false, "08/21/24", "405-876-5432", "sunvalleysupermarket.com", "500 SE 10th St, Oklahoma City", 430.70));

            productsList.add(new Product("400lb of Sweet Corn", "Golden Fields Market is a family-run grocery store that specializes in farm-fresh produce, dairy, and meats. Working directly with farmers, it ensures only the highest quality goods reach its shelves. With a commitment to sustainability and fair trade, Golden Fields Market promotes ethical sourcing while providing customers with nutritious and delicious options. Whether shopping for a meal or stocking up for the week, customers trust this store for fresh, wholesome ingredients.",
                    "Golden Fields Market", R.drawable.picnic, false, "08/23/24", "405-678-2345", "goldenfieldsmarket.com", "325 W Main St, Edmond", 395.90));

            productsList.add(new Product("600lb of Lettuce", "Fresh Bites Deli is a farm-to-table eatery that focuses on using fresh, locally grown ingredients in every dish. From crisp salads to hearty sandwiches, every meal is crafted with high-quality produce, ensuring exceptional taste and nutrition. The deli partners with local farmers to maintain sustainable sourcing and support regional agriculture. Whether dining in or taking out, customers enjoy flavorful, wholesome meals prepared with the freshest farm-to-table ingredients.",
                    "Fresh Bites Deli", R.drawable.picnic, false, "08/25/24", "405-543-6789", "freshbitesdeli.com", "440 S Broadway, Moore", 375.20));

            productsList.add(new Product("300lb of Green Beans", "Nature’s Harvest Grocers is a health-conscious supermarket that offers fresh, organic, and farm-sourced produce. With a strong emphasis on sustainability, it collaborates with local farmers to provide customers with top-quality vegetables, fruits, and dairy. The store promotes natural and chemical-free products, ensuring a healthy shopping experience. Customers appreciate the supermarket’s dedication to quality, affordability, and community support, making it a preferred destination for fresh and wholesome foods.",
                    "Nature’s Harvest Grocers", R.drawable.picnic, false, "08/27/24", "405-234-8765", "naturesharvestgrocers.com", "510 NE 23rd St, Oklahoma City", 385.50));

            productsList.add(new Product("450lb of Apples", "The Orchard Market is a specialty grocery store dedicated to providing farm-fresh fruits, vegetables, and organic pantry staples. Working closely with family-owned farms, it ensures a steady supply of seasonal produce at peak freshness. The market also features artisanal products and farm-direct dairy. With a focus on community, sustainability, and affordability, The Orchard Market remains a top choice for those looking for premium-quality, locally sourced groceries.",
                    "The Orchard Market", R.drawable.picnic, false, "08/29/24", "405-321-9998", "theorchardmarket.com", "1800 S Lincoln Blvd, Oklahoma City", 420.80));

            productsList.add(new Product("350lb of Blueberries", "Pure Earth Café is a farm-to-table restaurant that celebrates fresh, natural ingredients. Sourcing directly from local farms, it offers nutrient-rich meals, including smoothie bowls, salads, and whole-grain dishes. Committed to sustainability, Pure Earth Café ensures that every ingredient used is organic and ethically sourced. With a menu that changes seasonally, customers enjoy a diverse selection of flavorful and wholesome meals that highlight the best of regional agriculture.",
                    "Pure Earth Café", R.drawable.picnic, false, "08/31/24", "405-444-5678", "pureearthcafe.com", "375 W Reno Ave, Oklahoma City", 310.25));

            productsList.add(new Product("500lb of Peppers", "The Market Hub is a trusted grocery store that specializes in fresh, high-quality produce, dairy, and meats sourced from regional farms. Customers rely on The Market Hub for nutritious, farm-fresh ingredients at competitive prices. With a strong focus on sustainability, the store supports local farmers and ensures its products meet the highest standards. Whether shopping for fresh fruits, vegetables, or specialty items, The Market Hub delivers quality and convenience.",
                    "The Market Hub", R.drawable.picnic, false, "09/02/24", "405-222-1111", "themarkethub.com", "600 NW 4th St, Norman", 440.90));

            productsList.add(new Product("275lb of Mangoes", "Tropical Bounty is a fresh produce supplier specializing in tropical fruits and vegetables. Working with both supermarkets and restaurants, it ensures a steady supply of high-quality, farm-direct products. Whether providing fresh mangoes, pineapples, or bananas, Tropical Bounty focuses on sustainable sourcing and peak freshness. Customers trust its reputation for delivering flavorful, nutritious produce, making it a top choice for businesses looking for premium farm-sourced tropical ingredients.",
                    "Tropical Bounty", R.drawable.picnic, false, "09/04/24", "405-777-6543", "tropicalbounty.com", "1500 SE 15th St, Oklahoma City", 385.60));
            productsList.add(new Product("400lb of Organic Carrots", "Green Valley Co-op is a trusted marketplace where local farmers sell their fresh produce directly to consumers. It offers a wide variety of organic vegetables, dairy, and farm-raised meats. Customers love its commitment to sustainability and ethical farming. Green Valley Co-op connects farmers with local restaurants and grocery stores, ensuring fresh, high-quality products reach tables quickly. It’s the perfect place for farm-to-table businesses looking for premium produce.",
                    "Green Valley Co-op", R.drawable.picnic, false, "09/06/24", "405-333-2100", "greenvalleycoop.com", "850 S Western Ave, Norman", 420.50));

            productsList.add(new Product("350lb of Romaine Lettuce", "Harvest Table Bistro is a farm-to-table restaurant that prides itself on using the freshest local ingredients in every dish. Partnering with regional farms, it offers seasonal salads, gourmet sandwiches, and hearty entrees packed with nutrition. Customers appreciate its focus on sustainability, organic produce, and ethically sourced meats. Whether dining in or ordering takeout, Harvest Table Bistro provides a flavorful and wholesome culinary experience for all guests.",
                    "Harvest Table Bistro", R.drawable.picnic, false, "09/08/24", "405-678-9101", "harvesttablebistro.com", "1234 NW 12th St, Edmond", 340.75));

            productsList.add(new Product("600lb of White Onions", "Farm Fresh Distributors is a wholesale produce supplier that sources directly from family-owned farms. Specializing in bulk orders of fresh fruits and vegetables, it provides high-quality produce to restaurants, grocery stores, and local markets. With a focus on freshness, affordability, and sustainability, Farm Fresh Distributors ensures businesses receive the best farm-direct ingredients. Its reliable delivery service makes it a go-to supplier for fresh, nutrient-rich food.",
                    "Farm Fresh Distributors", R.drawable.picnic, false, "09/10/24", "405-876-4321", "farmfreshdistributors.com", "760 SE 3rd St, Oklahoma City", 510.60));

            productsList.add(new Product("500lb of Cucumbers", "Sunshine Market is a health-focused grocery store offering a diverse selection of farm-sourced produce. With a mission to provide fresh, organic, and locally grown fruits and vegetables, it supports sustainable agriculture and small farms. Customers can shop in-store or order online for convenient home delivery. The market also offers an extensive selection of gluten-free, vegan, and non-GMO products, making it a top choice for health-conscious shoppers.",
                    "Sunshine Market", R.drawable.picnic, false, "09/12/24", "405-543-2121", "sunshinemarket.com", "500 E Main St, Moore", 385.80));

            productsList.add(new Product("400lb of Bell Peppers", "Rustic Roots Café is a farm-to-table eatery that transforms fresh, local ingredients into delicious meals. Whether it’s organic salads, handmade sandwiches, or fresh fruit smoothies, every dish is crafted with care. The café sources ingredients from nearby farms, ensuring peak freshness and flavor. Rustic Roots Café is a community favorite, known for its commitment to sustainability and providing wholesome, nutritious options for diners of all ages.",
                    "Rustic Roots Café", R.drawable.picnic, false, "09/14/24", "405-654-7890", "rusticrootscafe.com", "325 S Robinson Ave, Oklahoma City", 320.40));

            productsList.add(new Product("275lb of Blackberries", "Blueberry Hill Market specializes in farm-fresh berries, organic fruits, and seasonal produce. It collaborates with local farmers to provide fresh, hand-picked ingredients for restaurants, grocery stores, and direct customers. The market also offers homemade jams, fresh juices, and farm-baked goods. With a focus on sustainability and quality, Blueberry Hill Market ensures that every purchase supports regional agriculture while delivering nutritious and delicious food to the community.",
                    "Blueberry Hill Market", R.drawable.picnic, false, "09/16/24", "405-777-1234", "blueberryhillmarket.com", "220 SE 8th St, Edmond", 290.90));

            productsList.add(new Product("450lb of Zucchini", "Farm to Fork Supply is a premium wholesale distributor of locally grown vegetables and herbs. Working directly with regional farmers, it ensures that grocery stores, restaurants, and food markets receive the highest quality produce available. With an emphasis on freshness and sustainability, Farm to Fork Supply helps businesses maintain a consistent stock of farm-fresh ingredients. Its reliable delivery service and commitment to ethical sourcing make it a top choice for businesses.",
                    "Farm to Fork Supply", R.drawable.picnic, false, "09/18/24", "405-888-5678", "farmtoforksupply.com", "670 W Reno Ave, Oklahoma City", 455.20));

            productsList.add(new Product("300lb of Peaches", "Golden Harvest Grocers is a family-owned supermarket that emphasizes fresh, locally sourced products. Its commitment to community agriculture ensures customers receive high-quality fruits, vegetables, and dairy. The store also features artisanal baked goods and farm-fresh eggs. Whether shopping for weekly groceries or specialty items, Golden Harvest Grocers provides a variety of fresh and organic options. Customers trust the store for nutritious food that supports local farmers and sustainable agriculture.",
                    "Golden Harvest Grocers", R.drawable.picnic, false, "09/20/24", "405-999-4321", "goldenharvestgrocers.com", "1400 S May Ave, Oklahoma City", 410.30));

            productsList.add(new Product("500lb of Red Cabbage", "The Local Pantry is a specialty grocery store that connects consumers with fresh, regional produce. It offers a selection of seasonal vegetables, dairy, meats, and organic pantry essentials sourced from nearby farms. Customers appreciate its focus on supporting small agricultural businesses while providing affordable, high-quality products. Whether looking for everyday ingredients or gourmet selections, The Local Pantry is the perfect place to shop for nutritious, locally sourced groceries.",
                    "The Local Pantry", R.drawable.picnic, false, "09/22/24", "405-121-3456", "thelocalpantry.com", "315 W 2nd St, Norman", 430.50));

            productsList.add(new Product("600lb of Pumpkins", "Autumn Farms Market is a seasonal grocery store that specializes in farm-fresh produce, especially fall harvest crops like pumpkins, squash, and apples. It works closely with local farmers to bring customers the best quality fruits and vegetables each season. The market also features homemade pies, fresh cider, and organic dairy products. Customers love the warm, rustic atmosphere and the ability to shop directly from farm-grown selections.",
                    "Autumn Farms Market", R.drawable.picnic, false, "09/24/24", "405-654-8888", "autumnfarmsmarket.com", "780 E Lincoln Blvd, Oklahoma City", 520.70));


            int temp;
        Product temp2;
        Product temp3;
//            for(int x = 0; x < productsList.size();x++){
//                temp = (int)(Math.random()* (productsList.size()-1));
//                temp2 = productsList.get(temp);
//                productsList.set(temp,productsList.get(x));
//                productsList.set(x,temp2);
//            }

            ForYouActivity.setFirst(false);
        }

        ProductAdapter adapter = new ProductAdapter(this);
        adapter.setProducts(productsList);
        products.setAdapter(adapter);
        products.setLayoutManager(new GridLayoutManager(this,1));


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search.setFocusableInTouchMode(true);
                search.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(search, InputMethodManager.SHOW_IMPLICIT);
            }
        });


        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                noResults.setVisibility(View.INVISIBLE);
                searchArrayList.clear();
                adapter.setProducts(searchArrayList);
                for (int x = 0; x < MainActivity2.getProductsList().size(); x++) {
                    if (ProfileActivity.isNotInsideSearch(MainActivity2.getProductsList().get(x).getProductName())) {
                        if (MainActivity2.getProductsList().get(x).getProductName().toLowerCase().indexOf(search.getText().toString().toLowerCase()) > -1 || MainActivity2.getProductsList().get(x).getLocation().toLowerCase().indexOf(search.getText().toString().toLowerCase()) > -1 || MainActivity2.getProductsList().get(x).getDate().indexOf(search.getText().toString()) > -1 || ("$" +Double.toString(MainActivity2.getProductsList().get(x).getPrice())).indexOf(search.getText().toString()) > -1 || MainActivity2.getProductsList().get(x).getPickupString().indexOf(search.getText().toString()) > -1) {
                            searchArrayList.add(0, MainActivity2.getProductsList().get(x));
                            adapter.setProducts(searchArrayList);


                        }
                    }
                }
                if(searchArrayList.isEmpty()){
                    noResults.setVisibility(View.VISIBLE);
                }
            }
        });
    }
    public void openMainActivity(){

        Intent intent = new Intent(this, MainActivity2.class);
        this.startActivity(intent);
    }
    public void openInboxActivity(){

        Intent intent = new Intent(this, InboxActivity.class);
        this.startActivity(intent);
    }
    public void openProfileActivity(){

        Intent intent = new Intent(this, ProfileActivity.class);
        this.startActivity(intent);
    }
    public void openForYouActivity(){

        Intent intent = new Intent(this, ForYouActivity.class);
        this.startActivity(intent);
    }
    public void openFollowingActivity(){

        Intent intent = new Intent(this, FollowingActivity.class);
        this.startActivity(intent);
    }

    public void openCalenderActivity(){

        Intent intent = new Intent(this, CalenderActivity.class);
        this.startActivity(intent);
    }
    public void openAIChatActivity(){

        Intent intent = new Intent(this, AiChatActivity.class);
        this.startActivity(intent);
    }

    public void openSearchActivity(){

        Intent intent = new Intent(this, SearchActivity.class);
        this.startActivity(intent);
    }
    public void openUserPage(){
        Intent intent = new Intent(getApplicationContext(), UserPage.class);
        getApplicationContext().startActivity(intent);
    }
    public static Product findProduct(String name){

        for(int x = 0; x < productsList.size();x++){
            if(productsList.get(x).getProductName().equals(name)){
                return productsList.get(x);
            }
        }
        return null;
    }
    public static boolean findProductInForYouBoolean(String name, String image){

        for(int x = 0; x < ForYouActivity.getForYouList().size();x++){
            if(ForYouActivity.getForYouList().get(x).getProductName().equals(name) && ForYouActivity.getForYouList().get(x).getProductImageURL().equals(image)){
                return true;
            }
        }
        return false;
    }
    public static boolean findProductInFollowingBoolean(String name, String image){

        for(int x = 0; x < FollowingActivity.getFollowingProductList().size();x++){
            if(FollowingActivity.getFollowingProductList().get(x).getProductName().equals(name) && FollowingActivity.getFollowingProductList().get(x).getProductImageURL().equals(image)){
                return true;
            }
        }
        return false;
    }
    public static ArrayList<Product> getProductsList(){
        return productsList;
    }

    public static void addToProdList(Product forNow){
        productsList.add(forNow);
    }


}