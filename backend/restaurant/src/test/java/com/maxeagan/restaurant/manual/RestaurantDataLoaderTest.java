package com.maxeagan.restaurant.manual;

import com.maxeagan.restaurant.domain.RestaurantCreateUpdateRequest;
import com.maxeagan.restaurant.domain.entities.Address;
import com.maxeagan.restaurant.domain.entities.OperatingHours;
import com.maxeagan.restaurant.domain.entities.Photo;
import com.maxeagan.restaurant.domain.entities.TimeRange;
import com.maxeagan.restaurant.services.PhotoService;
import com.maxeagan.restaurant.services.RestaurantService;
import com.maxeagan.restaurant.services.impl.RandomKansasCityGeoLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Rollback;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class RestaurantDataLoaderTest {

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private RandomKansasCityGeoLocationService geoLocationService;

    @Autowired
    private PhotoService photoService;

    @Autowired
    private ResourceLoader resourceLoader;

    @Test
    @Rollback(false) // Allow changes to persist
    public void createSampleRestaurants() throws Exception {
        List<RestaurantCreateUpdateRequest> restaurants = createRestaurantData();
        restaurants.forEach(restaurant -> {
            String fileName = restaurant.getPhotoIds().getFirst();
            Resource resource = resourceLoader.getResource("classpath:testdata/" + fileName);
            MultipartFile multipartFile = null;
            try {
                multipartFile = new MockMultipartFile(
                        "file", // parameter name
                        fileName, // original filename
                        MediaType.IMAGE_PNG_VALUE,
                        resource.getInputStream()
                );
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


            // Call the service method
            Photo uploadedPhoto = photoService.uploadPhoto(multipartFile);

            restaurant.setPhotoIds(List.of(uploadedPhoto.getUrl()));

            restaurantService.createRestaurant(restaurant);

            System.out.println("Created restaurant: " + restaurant.getName());
        });
    }

    private List<RestaurantCreateUpdateRequest> createRestaurantData() {
        return Arrays.asList(
                createRestaurant(
                        "The Golden Dragon",
                        "Chinese",
                        "+1 816-555-1101",
                        createAddress("12", "Gerrard Street", null, "Kansas City", "MO", "64101", "USA"),
                        createStandardOperatingHours("11:30", "23:00", "11:30", "23:30"),
                        "golden-dragon.png"
                ),
                createRestaurant(
                        "La Petite Maison",
                        "French",
                        "+1 816-555-1102",
                        createAddress("54", "Brook Street", null, "Kansas City", "MO", "64102", "USA"),
                        createStandardOperatingHours("12:00", "22:30", "12:00", "23:00"),
                        "la-petit-maison.png"
                ),
                createRestaurant(
                        "Raj Pavilion",
                        "Indian",
                        "+1 816-555-1103",
                        createAddress("27", "Brick Lane", null, "Kansas City", "MO", "64103", "USA"),
                        createStandardOperatingHours("12:00", "23:00", "12:00", "23:30"),
                        "raj-pavilion.png"
                ),
                createRestaurant(
                        "Sushi Master",
                        "Japanese",
                        "+1 816-555-1104",
                        createAddress("8", "Poland Street", null, "Kansas City", "MO", "64104", "USA"),
                        createStandardOperatingHours("11:30", "22:00", "11:30", "22:30"),
                        "sushi-master.png"
                ),
                createRestaurant(
                        "The Rustic Olive",
                        "Italian",
                        "+1 816-555-1105",
                        createAddress("92", "Dean Street", null, "Kansas City", "MO", "64105", "USA"),
                        createStandardOperatingHours("11:00", "23:00", "11:00", "23:30"),
                        "rustic-olive.png"
                ),
                createRestaurant(
                        "El Toro",
                        "Spanish",
                        "+1 816-555-1106",
                        createAddress("15", "Charlotte Street", null, "Kansas City", "MO", "64106", "USA"),
                        createStandardOperatingHours("12:00", "23:00", "12:00", "23:30"),
                        "el-toro.png"
                ),
                createRestaurant(
                        "The Greek House",
                        "Greek",
                        "+1 816-555-1107",
                        createAddress("32", "Store Street", null, "Kansas City", "MO", "64107", "USA"),
                        createStandardOperatingHours("12:00", "22:30", "12:00", "23:00"),
                        "greek-house.png"
                ),
                createRestaurant(
                        "Seoul Kitchen",
                        "Korean",
                        "+1 816-555-1108",
                        createAddress("71", "St John Street", null, "Kansas City", "MO", "64108", "USA"),
                        createStandardOperatingHours("11:30", "22:00", "11:30", "22:30"),
                        "seoul-kitchen.png"
                ),
                createRestaurant(
                        "Thai Orchid",
                        "Thai",
                        "+1 816-555-1109",
                        createAddress("45", "Warren Street", null, "Kansas City", "MO", "64109", "USA"),
                        createStandardOperatingHours("11:00", "22:30", "11:00", "23:00"),
                        "thai-orchid.png"
                ),
                createRestaurant(
                        "The Burger Joint",
                        "American",
                        "+1 816-555-1110",
                        createAddress("88", "Commercial Street", null, "Kansas City", "MO", "64110", "USA"),
                        createStandardOperatingHours("11:00", "23:00", "11:00", "23:30"),
                        "burger-joint.png"
                )
        );
    }


    private RestaurantCreateUpdateRequest createRestaurant(
            String name,
            String cuisineType,
            String contactInformation,
            Address address,
            OperatingHours operatingHours,
            String photoId
    ) {
        return RestaurantCreateUpdateRequest.builder()
                .name(name)
                .cuisineType(cuisineType)
                .contactInformation(contactInformation)
                .address(address)
                .operatingHours(operatingHours)
                .photoIds(List.of(photoId))
                .build();
    }

    private Address createAddress(
            String streetNumber,
            String streetName,
            String unit,
            String city,
            String state,
            String postalCode,
            String country
    ) {
        Address address = new Address();
        address.setStreetNumber(streetNumber);
        address.setStreetName(streetName);
        address.setUnit(unit);
        address.setCity(city);
        address.setState(state);
        address.setPostalCode(postalCode);
        address.setCountry(country);
        return address;
    }

    private OperatingHours createStandardOperatingHours(
            String weekdayOpen,
            String weekdayClose,
            String weekendOpen,
            String weekendClose
    ) {
        TimeRange weekday = new TimeRange();
        weekday.setOpenTime(weekdayOpen);
        weekday.setCloseTime(weekdayClose);

        TimeRange weekend = new TimeRange();
        weekend.setOpenTime(weekendOpen);
        weekend.setCloseTime(weekendClose);

        OperatingHours hours = new OperatingHours();
        hours.setMonday(weekday);
        hours.setTuesday(weekday);
        hours.setWednesday(weekday);
        hours.setThursday(weekday);
        hours.setFriday(weekend);
        hours.setSaturday(weekend);
        hours.setSunday(weekend);

        return hours;
    }
}
