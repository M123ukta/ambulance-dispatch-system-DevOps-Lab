public class AmbulanceSystemTest {

    static int passed = 0;
    static int failed = 0;

    public static void main(String[] args) {

        System.out.println("==============================================");
        System.out.println("   AMBULANCE DISPATCH SYSTEM - TEST SUITE");
        System.out.println("==============================================\n");

        // =====================================================
        // BASIC / POSITIVE TESTS
        // =====================================================

        test01CreateSystem();
        test02AddBasicAmbulance();
        test03AddALS();
        test04AddICUAmbulance();
        test05CreateNormalEmergency();
        test06CreateModerateEmergency();
        test07CreateHighEmergency();
        test08CreateCriticalEmergency();

        // =====================================================
        // EMERGENCY VALIDATION TESTS
        // =====================================================

        test09EmptyEmergencyId();
        test10EmptyPatientId();
        test11NullEmergencyType();
        test12EmptyPickupLocation();
        test13EmptyHospital();
        test14NegativeEmergencyDistance();
        test15DuplicateEmergencyId();

        // =====================================================
        // AMBULANCE VALIDATION TESTS
        // =====================================================

        test16EmptyAmbulanceId();
        test17NullDriver();
        test18NegativeAmbulanceDistance();
        test19DuplicateAmbulanceId();

        // =====================================================
        // PRIORITY TESTS
        // =====================================================

        test20CriticalHigherThanHigh();
        test21HighHigherThanModerate();
        test22ModerateHigherThanNormal();
        test23CriticalPriorityValue();

        // =====================================================
        // DISPATCH TESTS
        // =====================================================

        test24CriticalGetsICU();
        test25NormalGetsAvailableAmbulance();
        test26NearestAmbulanceSelected();
        test27UnavailableAmbulanceRejected();
        test28WrongAmbulanceTypeRejected();
        test29AmbulanceCannotBeAssignedTwice();

        // =====================================================
        // STATE TRANSITION TESTS
        // =====================================================

        test30AvailableToDispatched();
        test31DispatchedToEnRoute();
        test32EnRouteToPatientPickedUp();
        test33PatientPickedUpToHospital();
        test34HospitalToAvailable();
        test35InvalidAvailableToHospital();
        test36InvalidEnRouteToAvailable();

        // =====================================================
        // QUEUE / AUTOMATIC ALLOCATION
        // =====================================================

        test37EmergencyWaitsWhenNoAmbulance();
        test38AutomaticAllocationAfterAmbulanceAvailable();

        // =====================================================
        // HISTORY / ETA
        // =====================================================

        test39EmergencyHistoryRecorded();
        test40ETAIsCalculatedCorrectly();

        // =====================================================
        // FINAL RESULT
        // =====================================================

        System.out.println("\n==============================================");
        System.out.println("              TEST SUMMARY");
        System.out.println("==============================================");
        System.out.println("Total Tests  : " + (passed + failed));
        System.out.println("Tests Passed : " + passed);
        System.out.println("Tests Failed : " + failed);
        System.out.println("==============================================");

        if (failed > 0) {
            System.out.println("BUILD RESULT: FAILED");
            System.exit(1);
        } else {
            System.out.println("BUILD RESULT: SUCCESS");
            System.exit(0);
        }
    }

    // =========================================================
    // TEST HELPER
    // =========================================================

    static void check(boolean condition, String testName) {

        if (condition) {

            System.out.println("PASS : " + testName);
            passed++;

        } else {

            System.out.println("FAIL : " + testName);
            failed++;
        }
    }

    // =========================================================
    // 1. CREATE SYSTEM
    // =========================================================

    static void test01CreateSystem() {

        AmbulanceSystem system = new AmbulanceSystem();

        check(
                system != null,
                "System object created successfully"
        );
    }

    // =========================================================
    // 2. ADD BASIC AMBULANCE
    // =========================================================

    static void test02AddBasicAmbulance() {

        try {

            AmbulanceSystem system = new AmbulanceSystem();

            AmbulanceSystem.Driver driver =
                    new AmbulanceSystem.Driver(
                            "Rahul",
                            "9876543210",
                            "DL001"
                    );

            AmbulanceSystem.Ambulance ambulance =
                    new AmbulanceSystem.Ambulance(
                            "AMB001",
                            AmbulanceSystem.AmbulanceType.BASIC,
                            driver,
                            5
                    );

            system.addAmbulance(ambulance);

            check(
                    system.getAmbulance("AMB001") != null,
                    "Basic ambulance added successfully"
            );

        } catch (Exception e) {

            check(false,
                    "Basic ambulance added successfully");
        }
    }

    // =========================================================
    // 3. ADD ALS
    // =========================================================

    static void test03AddALS() {

        try {

            AmbulanceSystem system = new AmbulanceSystem();

            AmbulanceSystem.Driver driver =
                    new AmbulanceSystem.Driver(
                            "Arjun",
                            "9876543211",
                            "DL002"
                    );

            AmbulanceSystem.Ambulance ambulance =
                    new AmbulanceSystem.Ambulance(
                            "ALS001",
                            AmbulanceSystem.AmbulanceType.ADVANCED_LIFE_SUPPORT,
                            driver,
                            8
                    );

            system.addAmbulance(ambulance);

            check(
                    system.getAmbulance("ALS001") != null,
                    "Advanced Life Support ambulance added"
            );

        } catch (Exception e) {

            check(false,
                    "Advanced Life Support ambulance added");
        }
    }

    // =========================================================
    // 4. ADD ICU
    // =========================================================

    static void test04AddICUAmbulance() {

        try {

            AmbulanceSystem system = new AmbulanceSystem();

            AmbulanceSystem.Driver driver =
                    new AmbulanceSystem.Driver(
                            "Vikram",
                            "9876543212",
                            "DL003"
                    );

            AmbulanceSystem.Ambulance ambulance =
                    new AmbulanceSystem.Ambulance(
                            "ICU001",
                            AmbulanceSystem.AmbulanceType.ICU,
                            driver,
                            12
                    );

            system.addAmbulance(ambulance);

            check(
                    system.getAmbulance("ICU001") != null,
                    "ICU ambulance added successfully"
            );

        } catch (Exception e) {

            check(false,
                    "ICU ambulance added successfully");
        }
    }

    // =========================================================
    // 5. NORMAL EMERGENCY
    // =========================================================

    static void test05CreateNormalEmergency() {

        try {

            AmbulanceSystem.Emergency emergency =
                    new AmbulanceSystem.Emergency(
                            "E001",
                            "P001",
                            AmbulanceSystem.EmergencyType.NORMAL,
                            "Vellore",
                            "CMC Hospital",
                            5
                    );

            check(
                    emergency.type ==
                            AmbulanceSystem.EmergencyType.NORMAL,
                    "Normal emergency created"
            );

        } catch (Exception e) {

            check(false,
                    "Normal emergency created");
        }
    }

    // =========================================================
    // 6. MODERATE EMERGENCY
    // =========================================================

    static void test06CreateModerateEmergency() {

        try {

            AmbulanceSystem.Emergency emergency =
                    new AmbulanceSystem.Emergency(
                            "E002",
                            "P002",
                            AmbulanceSystem.EmergencyType.MODERATE,
                            "Katpadi",
                            "CMC Hospital",
                            7
                    );

            check(
                    emergency.type ==
                            AmbulanceSystem.EmergencyType.MODERATE,
                    "Moderate emergency created"
            );

        } catch (Exception e) {

            check(false,
                    "Moderate emergency created");
        }
    }

    // =========================================================
    // 7. HIGH EMERGENCY
    // =========================================================

    static void test07CreateHighEmergency() {

        try {

            AmbulanceSystem.Emergency emergency =
                    new AmbulanceSystem.Emergency(
                            "E003",
                            "P003",
                            AmbulanceSystem.EmergencyType.HIGH,
                            "Ranipet",
                            "CMC Hospital",
                            10
                    );

            check(
                    emergency.type ==
                            AmbulanceSystem.EmergencyType.HIGH,
                    "High emergency created"
            );

        } catch (Exception e) {

            check(false,
                    "High emergency created");
        }
    }

    // =========================================================
    // 8. CRITICAL EMERGENCY
    // =========================================================

    static void test08CreateCriticalEmergency() {

        try {

            AmbulanceSystem.Emergency emergency =
                    new AmbulanceSystem.Emergency(
                            "E004",
                            "P004",
                            AmbulanceSystem.EmergencyType.CRITICAL,
                            "Vellore",
                            "CMC Hospital",
                            15
                    );

            check(
                    emergency.type ==
                            AmbulanceSystem.EmergencyType.CRITICAL,
                    "Critical emergency created"
            );

        } catch (Exception e) {

            check(false,
                    "Critical emergency created");
        }
    }

    // =========================================================
    // 9. EMPTY EMERGENCY ID
    // =========================================================

    static void test09EmptyEmergencyId() {

        try {

            new AmbulanceSystem.Emergency(
                    "",
                    "P001",
                    AmbulanceSystem.EmergencyType.HIGH,
                    "Vellore",
                    "CMC",
                    5
            );

            check(false,
                    "Empty emergency ID rejected");

        } catch (AmbulanceSystem.InvalidEmergencyException e) {

            check(true,
                    "Empty emergency ID rejected");
        }
    }

    // =========================================================
    // 10. EMPTY PATIENT ID
    // =========================================================

    static void test10EmptyPatientId() {

        try {

            new AmbulanceSystem.Emergency(
                    "E010",
                    "",
                    AmbulanceSystem.EmergencyType.HIGH,
                    "Vellore",
                    "CMC",
                    5
            );

            check(false,
                    "Empty patient ID rejected");

        } catch (AmbulanceSystem.InvalidEmergencyException e) {

            check(true,
                    "Empty patient ID rejected");
        }
    }

    // =========================================================
    // 11. NULL EMERGENCY TYPE
    // =========================================================

    static void test11NullEmergencyType() {

        try {

            new AmbulanceSystem.Emergency(
                    "E011",
                    "P011",
                    null,
                    "Vellore",
                    "CMC",
                    5
            );

            check(false,
                    "Null emergency type rejected");

        } catch (AmbulanceSystem.InvalidEmergencyException e) {

            check(true,
                    "Null emergency type rejected");
        }
    }

    // =========================================================
    // 12. EMPTY PICKUP LOCATION
    // =========================================================

    static void test12EmptyPickupLocation() {

        try {

            new AmbulanceSystem.Emergency(
                    "E012",
                    "P012",
                    AmbulanceSystem.EmergencyType.NORMAL,
                    "",
                    "CMC",
                    5
            );

            check(false,
                    "Empty pickup location rejected");

        } catch (AmbulanceSystem.InvalidEmergencyException e) {

            check(true,
                    "Empty pickup location rejected");
        }
    }

    // =========================================================
    // 13. EMPTY HOSPITAL
    // =========================================================

    static void test13EmptyHospital() {

        try {

            new AmbulanceSystem.Emergency(
                    "E013",
                    "P013",
                    AmbulanceSystem.EmergencyType.NORMAL,
                    "Vellore",
                    "",
                    5
            );

            check(false,
                    "Empty destination hospital rejected");

        } catch (AmbulanceSystem.InvalidEmergencyException e) {

            check(true,
                    "Empty destination hospital rejected");
        }
    }

    // =========================================================
    // 14. NEGATIVE DISTANCE
    // =========================================================

    static void test14NegativeEmergencyDistance() {

        try {

            new AmbulanceSystem.Emergency(
                    "E014",
                    "P014",
                    AmbulanceSystem.EmergencyType.HIGH,
                    "Vellore",
                    "CMC",
                    -10
            );

            check(false,
                    "Negative emergency distance rejected");

        } catch (AmbulanceSystem.InvalidEmergencyException e) {

            check(true,
                    "Negative emergency distance rejected");
        }
    }

    // =========================================================
    // 15. DUPLICATE EMERGENCY
    // =========================================================

    static void test15DuplicateEmergencyId() {

        try {

            AmbulanceSystem system =
                    new AmbulanceSystem();

            AmbulanceSystem.Emergency e1 =
                    new AmbulanceSystem.Emergency(
                            "E015",
                            "P015",
                            AmbulanceSystem.EmergencyType.NORMAL,
                            "Vellore",
                            "CMC",
                            5
                    );

            AmbulanceSystem.Emergency e2 =
                    new AmbulanceSystem.Emergency(
                            "E015",
                            "P016",
                            AmbulanceSystem.EmergencyType.HIGH,
                            "Katpadi",
                            "CMC",
                            6
                    );

            system.addEmergency(e1);
            system.addEmergency(e2);

            check(false,
                    "Duplicate emergency ID rejected");

        } catch (IllegalArgumentException e) {

            check(true,
                    "Duplicate emergency ID rejected");

        } catch (Exception e) {

            check(false,
                    "Duplicate emergency ID rejected");
        }
    }

    // =========================================================
    // 16. EMPTY AMBULANCE ID
    // =========================================================

    static void test16EmptyAmbulanceId() {

        try {

            AmbulanceSystem.Driver driver =
                    new AmbulanceSystem.Driver(
                            "Driver",
                            "9999999999",
                            "L001"
                    );

            new AmbulanceSystem.Ambulance(
                    "",
                    AmbulanceSystem.AmbulanceType.BASIC,
                    driver,
                    5
            );

            check(false,
                    "Empty ambulance ID rejected");

        } catch (IllegalArgumentException e) {

            check(true,
                    "Empty ambulance ID rejected");
        }
    }

    // =========================================================
    // 17. NULL DRIVER
    // =========================================================

    static void test17NullDriver() {

        try {

            new AmbulanceSystem.Ambulance(
                    "A17",
                    AmbulanceSystem.AmbulanceType.BASIC,
                    null,
                    5
            );

            check(false,
                    "Null driver details rejected");

        } catch (IllegalArgumentException e) {

            check(true,
                    "Null driver details rejected");
        }
    }

    // =========================================================
    // 18. NEGATIVE AMBULANCE DISTANCE
    // =========================================================

    static void test18NegativeAmbulanceDistance() {

        try {

            AmbulanceSystem.Driver driver =
                    new AmbulanceSystem.Driver(
                            "Driver",
                            "9999999999",
                            "L001"
                    );

            new AmbulanceSystem.Ambulance(
                    "A18",
                    AmbulanceSystem.AmbulanceType.BASIC,
                    driver,
                    -5
            );

            check(false,
                    "Negative ambulance distance rejected");

        } catch (IllegalArgumentException e) {

            check(true,
                    "Negative ambulance distance rejected");
        }
    }

    // =========================================================
    // 19. DUPLICATE AMBULANCE
    // =========================================================

    static void test19DuplicateAmbulanceId() {

        try {

            AmbulanceSystem system =
                    new AmbulanceSystem();

            AmbulanceSystem.Driver driver =
                    new AmbulanceSystem.Driver(
                            "Driver",
                            "999",
                            "L001"
                    );

            system.addAmbulance(
                    new AmbulanceSystem.Ambulance(
                            "A19",
                            AmbulanceSystem.AmbulanceType.BASIC,
                            driver,
                            5
                    )
            );

            system.addAmbulance(
                    new AmbulanceSystem.Ambulance(
                            "A19",
                            AmbulanceSystem.AmbulanceType.ICU,
                            driver,
                            10
                    )
            );

            check(false,
                    "Duplicate ambulance ID rejected");

        } catch (IllegalArgumentException e) {

            check(true,
                    "Duplicate ambulance ID rejected");
        }
    }

    // =========================================================
    // 20. CRITICAL > HIGH
    // =========================================================

    static void test20CriticalHigherThanHigh() {

        check(
                AmbulanceSystem.EmergencyType.CRITICAL
                        .getPriority()
                        <
                AmbulanceSystem.EmergencyType.HIGH
                        .getPriority(),

                "Critical priority is higher than High"
        );
    }

    // =========================================================
    // 21. HIGH > MODERATE
    // =========================================================

    static void test21HighHigherThanModerate() {

        check(
                AmbulanceSystem.EmergencyType.HIGH
                        .getPriority()
                        <
                AmbulanceSystem.EmergencyType.MODERATE
                        .getPriority(),

                "High priority is higher than Moderate"
        );
    }

    // =========================================================
    // 22. MODERATE > NORMAL
    // =========================================================

    static void test22ModerateHigherThanNormal() {

        check(
                AmbulanceSystem.EmergencyType.MODERATE
                        .getPriority()
                        <
                AmbulanceSystem.EmergencyType.NORMAL
                        .getPriority(),

                "Moderate priority is higher than Normal"
        );
    }

    // =========================================================
    // 23. CRITICAL PRIORITY VALUE
    // =========================================================

    static void test23CriticalPriorityValue() {

        check(
                AmbulanceSystem.EmergencyType.CRITICAL
                        .getPriority() == 1,

                "Critical emergency has priority value 1"
        );
    }

    // =========================================================
    // 24. CRITICAL GETS ICU
    // =========================================================

    static void test24CriticalGetsICU() {

        try {

            AmbulanceSystem system =
                    new AmbulanceSystem();

            AmbulanceSystem.Driver driver =
                    new AmbulanceSystem.Driver(
                            "ICU Driver",
                            "999",
                            "L024"
                    );

            AmbulanceSystem.Ambulance ambulance =
                    new AmbulanceSystem.Ambulance(
                            "ICU24",
                            AmbulanceSystem.AmbulanceType.ICU,
                            driver,
                            10
                    );

            system.addAmbulance(ambulance);

            AmbulanceSystem.Emergency emergency =
                    new AmbulanceSystem.Emergency(
                            "E024",
                            "P024",
                            AmbulanceSystem.EmergencyType.CRITICAL,
                            "Vellore",
                            "CMC",
                            10
                    );

            system.addEmergency(emergency);

            check(
                    emergency.assignedAmbulance != null &&
                    emergency.assignedAmbulance.ambulanceId
                            .equals("ICU24"),

                    "Critical emergency receives ICU ambulance"
            );

        } catch (Exception e) {

            check(false,
                    "Critical emergency receives ICU ambulance");
        }
    }

    // =========================================================
    // 25. NORMAL GETS AMBULANCE
    // =========================================================

    static void test25NormalGetsAvailableAmbulance() {

        try {

            AmbulanceSystem system =
                    new AmbulanceSystem();

            AmbulanceSystem.Driver driver =
                    new AmbulanceSystem.Driver(
                            "Driver",
                            "999",
                            "L025"
                    );

            AmbulanceSystem.Ambulance ambulance =
                    new AmbulanceSystem.Ambulance(
                            "A25",
                            AmbulanceSystem.AmbulanceType.BASIC,
                            driver,
                            5
                    );

            system.addAmbulance(ambulance);

            AmbulanceSystem.Emergency emergency =
                    new AmbulanceSystem.Emergency(
                            "E025",
                            "P025",
                            AmbulanceSystem.EmergencyType.NORMAL,
                            "Vellore",
                            "CMC",
                            5
                    );

            system.addEmergency(emergency);

            check(
                    emergency.assignedAmbulance != null,
                    "Normal emergency receives available ambulance"
            );

        } catch (Exception e) {

            check(false,
                    "Normal emergency receives available ambulance");
        }
    }

    // =========================================================
    // 26. NEAREST AMBULANCE
    // =========================================================

    static void test26NearestAmbulanceSelected() {

        try {

            AmbulanceSystem system =
                    new AmbulanceSystem();

            AmbulanceSystem.Driver d1 =
                    new AmbulanceSystem.Driver(
                            "Driver 1", "111", "L1");

            AmbulanceSystem.Driver d2 =
                    new AmbulanceSystem.Driver(
                            "Driver 2", "222", "L2");

            system.addAmbulance(
                    new AmbulanceSystem.Ambulance(
                            "FAR",
                            AmbulanceSystem.AmbulanceType.BASIC,
                            d1,
                            20
                    )
            );

            system.addAmbulance(
                    new AmbulanceSystem.Ambulance(
                            "NEAR",
                            AmbulanceSystem.AmbulanceType.BASIC,
                            d2,
                            5
                    )
            );

            AmbulanceSystem.Emergency emergency =
                    new AmbulanceSystem.Emergency(
                            "E026",
                            "P026",
                            AmbulanceSystem.EmergencyType.NORMAL,
                            "Vellore",
                            "CMC",
                            5
                    );

            system.addEmergency(emergency);

            check(
                    emergency.assignedAmbulance.ambulanceId
                            .equals("NEAR"),

                    "Nearest suitable ambulance selected"
            );

        } catch (Exception e) {

            check(false,
                    "Nearest suitable ambulance selected");
        }
    }

    // =========================================================
    // 27. UNAVAILABLE AMBULANCE
    // =========================================================

    static void test27UnavailableAmbulanceRejected() {

        try {

            AmbulanceSystem system =
                    new AmbulanceSystem();

            AmbulanceSystem.Driver driver =
                    new AmbulanceSystem.Driver(
                            "Driver",
                            "999",
                            "L027"
                    );

            AmbulanceSystem.Ambulance ambulance =
                    new AmbulanceSystem.Ambulance(
                            "A27",
                            AmbulanceSystem.AmbulanceType.BASIC,
                            driver,
                            5
                    );

            system.addAmbulance(ambulance);

            ambulance.changeState(
                    AmbulanceSystem.AmbulanceState.DISPATCHED
            );

            AmbulanceSystem.Emergency emergency =
                    new AmbulanceSystem.Emergency(
                            "E027",
                            "P027",
                            AmbulanceSystem.EmergencyType.NORMAL,
                            "Vellore",
                            "CMC",
                            5
                    );

            try {

                system.dispatchEmergency(
                        emergency,
                        ambulance
                );

                check(false,
                        "Unavailable ambulance rejected");

            } catch (
                    AmbulanceSystem.AmbulanceUnavailableException e) {

                check(true,
                        "Unavailable ambulance rejected");
            }

        } catch (Exception e) {

            check(false,
                    "Unavailable ambulance rejected");
        }
    }

    // =========================================================
    // 28. WRONG AMBULANCE TYPE
    // =========================================================

    static void test28WrongAmbulanceTypeRejected() {

        try {

            AmbulanceSystem system =
                    new AmbulanceSystem();

            AmbulanceSystem.Driver driver =
                    new AmbulanceSystem.Driver(
                            "Driver",
                            "999",
                            "L028"
                    );

            AmbulanceSystem.Ambulance ambulance =
                    new AmbulanceSystem.Ambulance(
                            "BASIC28",
                            AmbulanceSystem.AmbulanceType.BASIC,
                            driver,
                            5
                    );

            system.addAmbulance(ambulance);

            AmbulanceSystem.Emergency emergency =
                    new AmbulanceSystem.Emergency(
                            "E028",
                            "P028",
                            AmbulanceSystem.EmergencyType.CRITICAL,
                            "Vellore",
                            "CMC",
                            5
                    );

            try {

                system.dispatchEmergency(
                        emergency,
                        ambulance
                );

                check(false,
                        "Unsuitable ambulance type rejected");

            } catch (
                    AmbulanceSystem.AmbulanceUnavailableException e) {

                check(true,
                        "Unsuitable ambulance type rejected");
            }

        } catch (Exception e) {

            check(false,
                    "Unsuitable ambulance type rejected");
        }
    }

    // =========================================================
    // 29. DOUBLE ASSIGNMENT
    // =========================================================

    static void test29AmbulanceCannotBeAssignedTwice() {

        try {

            AmbulanceSystem system =
                    new AmbulanceSystem();

            AmbulanceSystem.Driver driver =
                    new AmbulanceSystem.Driver(
                            "Driver",
                            "999",
                            "L029"
                    );

            AmbulanceSystem.Ambulance ambulance =
                    new AmbulanceSystem.Ambulance(
                            "A29",
                            AmbulanceSystem.AmbulanceType.ICU,
                            driver,
                            5
                    );

            system.addAmbulance(ambulance);

            AmbulanceSystem.Emergency e1 =
                    new AmbulanceSystem.Emergency(
                            "E029A",
                            "P029A",
                            AmbulanceSystem.EmergencyType.CRITICAL,
                            "Vellore",
                            "CMC",
                            5
                    );

            system.addEmergency(e1);

            AmbulanceSystem.Emergency e2 =
                    new AmbulanceSystem.Emergency(
                            "E029B",
                            "P029B",
                            AmbulanceSystem.EmergencyType.CRITICAL,
                            "Katpadi",
                            "CMC",
                            5
                    );

            try {

                system.dispatchEmergency(
                        e2,
                        ambulance
                );

                check(false,
                        "Ambulance cannot be assigned twice");

            } catch (
                    AmbulanceSystem.AmbulanceUnavailableException e) {

                check(true,
                        "Ambulance cannot be assigned twice");
            }

        } catch (Exception e) {

            check(false,
                    "Ambulance cannot be assigned twice");
        }
    }

    // =========================================================
    // 30. AVAILABLE -> DISPATCHED
    // =========================================================

    static void test30AvailableToDispatched() {

        try {

            AmbulanceSystem.Driver driver =
                    new AmbulanceSystem.Driver(
                            "Driver", "999", "L030");

            AmbulanceSystem.Ambulance ambulance =
                    new AmbulanceSystem.Ambulance(
                            "A30",
                            AmbulanceSystem.AmbulanceType.BASIC,
                            driver,
                            5
                    );

            ambulance.changeState(
                    AmbulanceSystem.AmbulanceState.DISPATCHED
            );

            check(
                    ambulance.state ==
                            AmbulanceSystem.AmbulanceState.DISPATCHED,
                    "Available -> Dispatched transition works"
            );

        } catch (Exception e) {

            check(false,
                    "Available -> Dispatched transition works");
        }
    }

    // =========================================================
    // 31. DISPATCHED -> EN ROUTE
    // =========================================================

    static void test31DispatchedToEnRoute() {

        try {

            AmbulanceSystem.Driver driver =
                    new AmbulanceSystem.Driver(
                            "Driver", "999", "L031");

            AmbulanceSystem.Ambulance ambulance =
                    new AmbulanceSystem.Ambulance(
                            "A31",
                            AmbulanceSystem.AmbulanceType.BASIC,
                            driver,
                            5
                    );

            ambulance.changeState(
                    AmbulanceSystem.AmbulanceState.DISPATCHED
            );

            ambulance.changeState(
                    AmbulanceSystem.AmbulanceState.EN_ROUTE
            );

            check(
                    ambulance.state ==
                            AmbulanceSystem.AmbulanceState.EN_ROUTE,
                    "Dispatched -> En Route transition works"
            );

        } catch (Exception e) {

            check(false,
                    "Dispatched -> En Route transition works");
        }
    }

    // =========================================================
    // 32. EN ROUTE -> PATIENT PICKED UP
    // =========================================================

    static void test32EnRouteToPatientPickedUp() {

        try {

            AmbulanceSystem.Driver driver =
                    new AmbulanceSystem.Driver(
                            "Driver", "999", "L032");

            AmbulanceSystem.Ambulance ambulance =
                    new AmbulanceSystem.Ambulance(
                            "A32",
                            AmbulanceSystem.AmbulanceType.BASIC,
                            driver,
                            5
                    );

            ambulance.changeState(
                    AmbulanceSystem.AmbulanceState.DISPATCHED
            );

            ambulance.changeState(
                    AmbulanceSystem.AmbulanceState.EN_ROUTE
            );

            ambulance.changeState(
                    AmbulanceSystem.AmbulanceState.PATIENT_PICKED_UP
            );

            check(
                    ambulance.state ==
                            AmbulanceSystem.AmbulanceState.PATIENT_PICKED_UP,
                    "En Route -> Patient Picked Up works"
            );

        } catch (Exception e) {

            check(false,
                    "En Route -> Patient Picked Up works");
        }
    }

    // =========================================================
    // 33. PATIENT PICKED UP -> HOSPITAL
    // =========================================================

    static void test33PatientPickedUpToHospital() {

        try {

            AmbulanceSystem.Driver driver =
                    new AmbulanceSystem.Driver(
                            "Driver", "999", "L033");

            AmbulanceSystem.Ambulance ambulance =
                    new AmbulanceSystem.Ambulance(
                            "A33",
                            AmbulanceSystem.AmbulanceType.BASIC,
                            driver,
                            5
                    );

            ambulance.changeState(
                    AmbulanceSystem.AmbulanceState.DISPATCHED
            );

            ambulance.changeState(
                    AmbulanceSystem.AmbulanceState.EN_ROUTE
            );

            ambulance.changeState(
                    AmbulanceSystem.AmbulanceState.PATIENT_PICKED_UP
            );

            ambulance.changeState(
                    AmbulanceSystem.AmbulanceState.HOSPITAL_ARRIVED
            );

            check(
                    ambulance.state ==
                            AmbulanceSystem.AmbulanceState.HOSPITAL_ARRIVED,
                    "Patient Picked Up -> Hospital Arrived works"
            );

        } catch (Exception e) {

            check(false,
                    "Patient Picked Up -> Hospital Arrived works");
        }
    }

    // =========================================================
    // 34. HOSPITAL -> AVAILABLE
    // =========================================================

    static void test34HospitalToAvailable() {

        try {

            AmbulanceSystem.Driver driver =
                    new AmbulanceSystem.Driver(
                            "Driver", "999", "L034");

            AmbulanceSystem.Ambulance ambulance =
                    new AmbulanceSystem.Ambulance(
                            "A34",
                            AmbulanceSystem.AmbulanceType.BASIC,
                            driver,
                            5
                    );

            ambulance.changeState(
                    AmbulanceSystem.AmbulanceState.DISPATCHED
            );

            ambulance.changeState(
                    AmbulanceSystem.AmbulanceState.EN_ROUTE
            );

            ambulance.changeState(
                    AmbulanceSystem.AmbulanceState.PATIENT_PICKED_UP
            );

            ambulance.changeState(
                    AmbulanceSystem.AmbulanceState.HOSPITAL_ARRIVED
            );

            ambulance.changeState(
                    AmbulanceSystem.AmbulanceState.AVAILABLE
            );

            check(
                    ambulance.state ==
                            AmbulanceSystem.AmbulanceState.AVAILABLE,
                    "Hospital Arrived -> Available works"
            );

        } catch (Exception e) {

            check(false,
                    "Hospital Arrived -> Available works");
        }
    }

    // =========================================================
    // 35. INVALID AVAILABLE -> HOSPITAL
    // =========================================================

    static void test35InvalidAvailableToHospital() {

        try {

            AmbulanceSystem.Driver driver =
                    new AmbulanceSystem.Driver(
                            "Driver", "999", "L035");

            AmbulanceSystem.Ambulance ambulance =
                    new AmbulanceSystem.Ambulance(
                            "A35",
                            AmbulanceSystem.AmbulanceType.BASIC,
                            driver,
                            5
                    );

            ambulance.changeState(
                    AmbulanceSystem.AmbulanceState.HOSPITAL_ARRIVED
            );

            check(false,
                    "Invalid Available -> Hospital transition rejected");

        } catch (
                AmbulanceSystem.InvalidStateTransitionException e) {

            check(true,
                    "Invalid Available -> Hospital transition rejected");
        }
    }

    // =========================================================
    // 36. INVALID EN ROUTE -> AVAILABLE
    // =========================================================

    static void test36InvalidEnRouteToAvailable() {

        try {

            AmbulanceSystem.Driver driver =
                    new AmbulanceSystem.Driver(
                            "Driver", "999", "L036");

            AmbulanceSystem.Ambulance ambulance =
                    new AmbulanceSystem.Ambulance(
                            "A36",
                            AmbulanceSystem.AmbulanceType.BASIC,
                            driver,
                            5
                    );

            ambulance.changeState(
                    AmbulanceSystem.AmbulanceState.DISPATCHED
            );

            ambulance.changeState(
                    AmbulanceSystem.AmbulanceState.EN_ROUTE
            );

            ambulance.changeState(
                    AmbulanceSystem.AmbulanceState.AVAILABLE
            );

            check(false,
                    "Invalid En Route -> Available transition rejected");

        } catch (
                AmbulanceSystem.InvalidStateTransitionException e) {

            check(true,
                    "Invalid En Route -> Available transition rejected");
        }
    }

    // =========================================================
    // 37. WAITING QUEUE
    // =========================================================

    static void test37EmergencyWaitsWhenNoAmbulance() {

        try {

            AmbulanceSystem system =
                    new AmbulanceSystem();

            AmbulanceSystem.Driver driver =
                    new AmbulanceSystem.Driver(
                            "Driver",
                            "999",
                            "L037"
                    );

            AmbulanceSystem.Ambulance ambulance =
                    new AmbulanceSystem.Ambulance(
                            "A37",
                            AmbulanceSystem.AmbulanceType.ICU,
                            driver,
                            5
                    );

            system.addAmbulance(ambulance);

            AmbulanceSystem.Emergency e1 =
                    new AmbulanceSystem.Emergency(
                            "E037A",
                            "P037A",
                            AmbulanceSystem.EmergencyType.CRITICAL,
                            "Vellore",
                            "CMC",
                            5
                    );

            AmbulanceSystem.Emergency e2 =
                    new AmbulanceSystem.Emergency(
                            "E037B",
                            "P037B",
                            AmbulanceSystem.EmergencyType.CRITICAL,
                            "Katpadi",
                            "CMC",
                            8
                    );

            system.addEmergency(e1);
            system.addEmergency(e2);

            check(
                    system.getWaitingQueueSize() == 1,
                    "Emergency waits when no ambulance is available"
            );

        } catch (Exception e) {

            check(false,
                    "Emergency waits when no ambulance is available");
        }
    }

    // =========================================================
    // 38. AUTOMATIC ALLOCATION
    // =========================================================

    static void test38AutomaticAllocationAfterAmbulanceAvailable() {

        try {

            AmbulanceSystem system =
                    new AmbulanceSystem();

            AmbulanceSystem.Driver driver =
                    new AmbulanceSystem.Driver(
                            "Driver",
                            "999",
                            "L038"
                    );

            AmbulanceSystem.Ambulance ambulance =
                    new AmbulanceSystem.Ambulance(
                            "A38",
                            AmbulanceSystem.AmbulanceType.ICU,
                            driver,
                            5
                    );

            system.addAmbulance(ambulance);

            AmbulanceSystem.Emergency e1 =
                    new AmbulanceSystem.Emergency(
                            "E038A",
                            "P038A",
                            AmbulanceSystem.EmergencyType.CRITICAL,
                            "Vellore",
                            "CMC",
                            5
                    );

            AmbulanceSystem.Emergency e2 =
                    new AmbulanceSystem.Emergency(
                            "E038B",
                            "P038B",
                            AmbulanceSystem.EmergencyType.CRITICAL,
                            "Katpadi",
                            "CMC",
                            5
                    );

            system.addEmergency(e1);
            system.addEmergency(e2);

            // Complete first emergency
            system.updateAmbulanceState(
                    "A38",
                    AmbulanceSystem.AmbulanceState.EN_ROUTE
            );

            system.updateAmbulanceState(
                    "A38",
                    AmbulanceSystem.AmbulanceState.PATIENT_PICKED_UP
            );

            system.updateAmbulanceState(
                    "A38",
                    AmbulanceSystem.AmbulanceState.HOSPITAL_ARRIVED
            );

            system.updateAmbulanceState(
                    "A38",
                    AmbulanceSystem.AmbulanceState.AVAILABLE
            );

            check(
                    e2.assignedAmbulance != null,
                    "Waiting emergency automatically allocated"
            );

        } catch (Exception e) {

            check(false,
                    "Waiting emergency automatically allocated");
        }
    }

    // =========================================================
    // 39. HISTORY
    // =========================================================

    static void test39EmergencyHistoryRecorded() {

        try {

            AmbulanceSystem system =
                    new AmbulanceSystem();

            AmbulanceSystem.Driver driver =
                    new AmbulanceSystem.Driver(
                            "Driver",
                            "999",
                            "L039"
                    );

            system.addAmbulance(
                    new AmbulanceSystem.Ambulance(
                            "A39",
                            AmbulanceSystem.AmbulanceType.ICU,
                            driver,
                            5
                    )
            );

            AmbulanceSystem.Emergency emergency =
                    new AmbulanceSystem.Emergency(
                            "E039",
                            "P039",
                            AmbulanceSystem.EmergencyType.CRITICAL,
                            "Vellore",
                            "CMC",
                            5
                    );

            system.addEmergency(emergency);

            check(
                    system.getHistorySize() > 0,
                    "Emergency history recorded"
            );

        } catch (Exception e) {

            check(false,
                    "Emergency history recorded");
        }
    }

    // =========================================================
    // 40. ETA
    // =========================================================

    static void test40ETAIsCalculatedCorrectly() {

        try {

            AmbulanceSystem system =
                    new AmbulanceSystem();

            AmbulanceSystem.Driver driver =
                    new AmbulanceSystem.Driver(
                            "Driver",
                            "999",
                            "L040"
                    );

            system.addAmbulance(
                    new AmbulanceSystem.Ambulance(
                            "A40",
                            AmbulanceSystem.AmbulanceType.ICU,
                            driver,
                            20
                    )
            );

            AmbulanceSystem.Emergency emergency =
                    new AmbulanceSystem.Emergency(
                            "E040",
                            "P040",
                            AmbulanceSystem.EmergencyType.CRITICAL,
                            "Vellore",
                            "CMC",
                            20
                    );

            system.addEmergency(emergency);

            // Speed = 40 km/h
            // Distance = 20 km
            // ETA = 20/40 * 60 = 30 minutes

            check(
                    emergency.estimatedArrivalMinutes == 30.0,
                    "Estimated arrival time calculated correctly"
            );

        } catch (Exception e) {

            check(false,
                    "Estimated arrival time calculated correctly");
        }
    }
}