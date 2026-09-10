import java.util.*;

public class AmbulanceSystem {

    // =========================================================
    // ENUMS
    // =========================================================

    enum EmergencyType {
        CRITICAL(1),
        HIGH(2),
        MODERATE(3),
        NORMAL(4);

        private final int priority;

        EmergencyType(int priority) {
            this.priority = priority;
        }

        public int getPriority() {
            return priority;
        }
    }

    enum AmbulanceType {
        BASIC,
        ADVANCED_LIFE_SUPPORT,
        ICU
    }

    enum AmbulanceState {
        AVAILABLE,
        DISPATCHED,
        EN_ROUTE,
        PATIENT_PICKED_UP,
        HOSPITAL_ARRIVED
    }

    enum EmergencyStatus {
        WAITING,
        ASSIGNED,
        IN_PROGRESS,
        COMPLETED
    }

    // =========================================================
    // CUSTOM EXCEPTIONS
    // =========================================================

    static class InvalidEmergencyException extends Exception {

        public InvalidEmergencyException(String message) {
            super(message);
        }
    }

    static class AmbulanceUnavailableException extends Exception {

        public AmbulanceUnavailableException(String message) {
            super(message);
        }
    }

    static class InvalidStateTransitionException extends Exception {

        public InvalidStateTransitionException(String message) {
            super(message);
        }
    }

    // =========================================================
    // DRIVER CLASS
    // =========================================================

    static class Driver {

        String name;
        String phone;
        String licenseNumber;

        Driver(String name, String phone, String licenseNumber) {

            if (name == null || name.isBlank()) {
                throw new IllegalArgumentException(
                        "Driver name cannot be empty");
            }

            if (phone == null || phone.isBlank()) {
                throw new IllegalArgumentException(
                        "Driver phone cannot be empty");
            }

            if (licenseNumber == null || licenseNumber.isBlank()) {
                throw new IllegalArgumentException(
                        "Driver license number cannot be empty");
            }

            this.name = name;
            this.phone = phone;
            this.licenseNumber = licenseNumber;
        }

        @Override
        public String toString() {

            return name +
                    " | Phone: " + phone +
                    " | License: " + licenseNumber;
        }
    }

    // =========================================================
    // AMBULANCE CLASS
    // =========================================================

    static class Ambulance {

        String ambulanceId;
        AmbulanceType type;
        AmbulanceState state;
        Driver driver;
        double currentDistance;

        Ambulance(
                String ambulanceId,
                AmbulanceType type,
                Driver driver,
                double currentDistance) {

            if (ambulanceId == null || ambulanceId.isBlank()) {
                throw new IllegalArgumentException(
                        "Ambulance ID cannot be empty");
            }

            if (type == null) {
                throw new IllegalArgumentException(
                        "Ambulance type cannot be null");
            }

            if (driver == null) {
                throw new IllegalArgumentException(
                        "Driver details are required");
            }

            if (currentDistance < 0) {
                throw new IllegalArgumentException(
                        "Distance cannot be negative");
            }

            this.ambulanceId = ambulanceId;
            this.type = type;
            this.driver = driver;
            this.currentDistance = currentDistance;
            this.state = AmbulanceState.AVAILABLE;
        }

        // -----------------------------------------------------
        // STATE TRANSITION
        // -----------------------------------------------------

        public void changeState(AmbulanceState newState)
                throws InvalidStateTransitionException {

            if (newState == null) {

                throw new InvalidStateTransitionException(
                        "New ambulance state cannot be null");
            }

            boolean validTransition = false;

            switch (state) {

                case AVAILABLE:

                    if (newState == AmbulanceState.DISPATCHED) {
                        validTransition = true;
                    }

                    break;

                case DISPATCHED:

                    if (newState == AmbulanceState.EN_ROUTE) {
                        validTransition = true;
                    }

                    break;

                case EN_ROUTE:

                    if (newState ==
                            AmbulanceState.PATIENT_PICKED_UP) {

                        validTransition = true;
                    }

                    break;

                case PATIENT_PICKED_UP:

                    if (newState ==
                            AmbulanceState.HOSPITAL_ARRIVED) {

                        validTransition = true;
                    }

                    break;

                case HOSPITAL_ARRIVED:

                    if (newState == AmbulanceState.AVAILABLE) {
                        validTransition = true;
                    }

                    break;
            }

            if (!validTransition) {

                throw new InvalidStateTransitionException(
                        "Invalid transition: " +
                                state +
                                " -> " +
                                newState);
            }

            state = newState;
        }

        @Override
        public String toString() {

            return ambulanceId +
                    " | Type: " + type +
                    " | State: " + state +
                    " | Driver: " + driver.name +
                    " | Distance: " +
                    currentDistance + " km";
        }
    }

    // =========================================================
    // EMERGENCY CLASS
    // =========================================================

    static class Emergency {

        String emergencyId;
        String patientId;
        EmergencyType type;
        String pickupLocation;
        String destinationHospital;
        double distance;

        EmergencyStatus status;

        Ambulance assignedAmbulance;

        double estimatedArrivalMinutes;

        Emergency(
                String emergencyId,
                String patientId,
                EmergencyType type,
                String pickupLocation,
                String destinationHospital,
                double distance)
                throws InvalidEmergencyException {

            // -------------------------------------------------
            // VALIDATION
            // -------------------------------------------------

            if (emergencyId == null ||
                    emergencyId.isBlank()) {

                throw new InvalidEmergencyException(
                        "Emergency ID cannot be empty");
            }

            if (patientId == null ||
                    patientId.isBlank()) {

                throw new InvalidEmergencyException(
                        "Patient ID cannot be empty");
            }

            if (type == null) {

                throw new InvalidEmergencyException(
                        "Emergency type is required");
            }

            if (pickupLocation == null ||
                    pickupLocation.isBlank()) {

                throw new InvalidEmergencyException(
                        "Pickup location cannot be empty");
            }

            if (destinationHospital == null ||
                    destinationHospital.isBlank()) {

                throw new InvalidEmergencyException(
                        "Destination hospital cannot be empty");
            }

            if (distance < 0) {

                throw new InvalidEmergencyException(
                        "Distance cannot be negative");
            }

            this.emergencyId = emergencyId;
            this.patientId = patientId;
            this.type = type;
            this.pickupLocation = pickupLocation;
            this.destinationHospital = destinationHospital;
            this.distance = distance;

            this.status = EmergencyStatus.WAITING;

            this.assignedAmbulance = null;

            this.estimatedArrivalMinutes = 0;
        }

        @Override
        public String toString() {

            String ambulanceInfo;

            if (assignedAmbulance == null) {

                ambulanceInfo = "Not Assigned";

            } else {

                ambulanceInfo =
                        assignedAmbulance.ambulanceId;
            }

            return emergencyId +
                    " | Patient: " + patientId +
                    " | Type: " + type +
                    " | Pickup: " + pickupLocation +
                    " | Hospital: " + destinationHospital +
                    " | Distance: " + distance + " km" +
                    " | Ambulance: " + ambulanceInfo +
                    " | Status: " + status +
                    " | ETA: " +
                    estimatedArrivalMinutes +
                    " minutes";
        }
    }

    // =========================================================
    // SYSTEM DATA STRUCTURES
    // =========================================================

    private final List<Ambulance> ambulances =
            new ArrayList<>();

    private final List<Emergency> emergencies =
            new ArrayList<>();

    /*
     * PriorityQueue ensures that:
     *
     * CRITICAL
     *     ↓
     * HIGH
     *     ↓
     * MODERATE
     *     ↓
     * NORMAL
     *
     * are processed in priority order.
     */

    private final PriorityQueue<Emergency> waitingQueue =
            new PriorityQueue<>(
                    Comparator.comparingInt(
                            emergency ->
                                    emergency.type.getPriority()
                    )
            );

    private final List<String> emergencyHistory =
            new ArrayList<>();

    // =========================================================
    // ADD AMBULANCE
    // =========================================================

    public void addAmbulance(Ambulance ambulance) {

        if (ambulance == null) {

            throw new IllegalArgumentException(
                    "Ambulance cannot be null");
        }

        // Check duplicate ambulance ID

        for (Ambulance existing : ambulances) {

            if (existing.ambulanceId.equalsIgnoreCase(
                    ambulance.ambulanceId)) {

                throw new IllegalArgumentException(
                        "Duplicate ambulance ID: " +
                                ambulance.ambulanceId);
            }
        }

        ambulances.add(ambulance);

        System.out.println(
                "Ambulance added: " +
                        ambulance.ambulanceId);
    }

    // =========================================================
    // ADD EMERGENCY
    // =========================================================

    public void addEmergency(Emergency emergency) {

        if (emergency == null) {

            throw new IllegalArgumentException(
                    "Emergency cannot be null");
        }

        // Check duplicate emergency ID

        for (Emergency existing : emergencies) {

            if (existing.emergencyId.equalsIgnoreCase(
                    emergency.emergencyId)) {

                throw new IllegalArgumentException(
                        "Duplicate emergency ID: " +
                                emergency.emergencyId);
            }
        }

        emergencies.add(emergency);

        waitingQueue.add(emergency);

        emergencyHistory.add(
                "Emergency " +
                        emergency.emergencyId +
                        " received - Priority: " +
                        emergency.type);

        System.out.println(
                "Emergency received: " +
                        emergency.emergencyId +
                        " | Priority: " +
                        emergency.type);

        automaticallyDispatch();
    }

    // =========================================================
    // CHECK AMBULANCE SUITABILITY
    // =========================================================

    private boolean isSuitable(
            Ambulance ambulance,
            Emergency emergency) {

        // Ambulance must be available

        if (ambulance.state !=
                AmbulanceState.AVAILABLE) {

            return false;
        }

        /*
         * Ambulance requirements:
         *
         * CRITICAL:
         * ICU or ALS
         *
         * HIGH:
         * ICU or ALS
         *
         * MODERATE:
         * Basic, ALS or ICU
         *
         * NORMAL:
         * Basic, ALS or ICU
         */

        switch (emergency.type) {

            case CRITICAL:

                return ambulance.type ==
                        AmbulanceType.ICU ||
                        ambulance.type ==
                        AmbulanceType.ADVANCED_LIFE_SUPPORT;

            case HIGH:

                return ambulance.type ==
                        AmbulanceType.ICU ||
                        ambulance.type ==
                        AmbulanceType.ADVANCED_LIFE_SUPPORT;

            case MODERATE:

                return true;

            case NORMAL:

                return true;

            default:

                return false;
        }
    }

    // =========================================================
    // AMBULANCE CAPABILITY SCORE
    // =========================================================

    private int getAmbulanceCapability(
            AmbulanceType type) {

        switch (type) {

            case BASIC:
                return 1;

            case ADVANCED_LIFE_SUPPORT:
                return 2;

            case ICU:
                return 3;

            default:
                return 0;
        }
    }

    // =========================================================
    // FIND BEST AMBULANCE
    // =========================================================

    private Ambulance findBestAmbulance(
            Emergency emergency) {

        List<Ambulance> suitableAmbulances =
                new ArrayList<>();

        // -----------------------------------------------------
        // STEP 1:
        // Find all available and medically suitable ambulances
        // -----------------------------------------------------

        for (Ambulance ambulance : ambulances) {

            if (isSuitable(ambulance, emergency)) {

                suitableAmbulances.add(ambulance);
            }
        }

        if (suitableAmbulances.isEmpty()) {

            return null;
        }

        /*
         * STEP 2:
         *
         * For Critical and High emergencies,
         * select the ambulance with the minimum
         * required medical capability.
         *
         * This avoids unnecessarily using an ICU
         * ambulance for an emergency that can be
         * handled by ALS.
         *
         * Example:
         *
         * Critical:
         * ICU = 3
         * ALS = 2
         *
         * ALS is preferred if available.
         *
         * However, if only ICU is available,
         * ICU will be selected.
         */

        int requiredCapability;

        if (emergency.type == EmergencyType.CRITICAL ||
                emergency.type == EmergencyType.HIGH) {

            requiredCapability = 2;

        } else {

            requiredCapability = 1;
        }

        // -----------------------------------------------------
        // STEP 3:
        // Find the minimum suitable capability available
        // -----------------------------------------------------

        int minimumCapability =
                Integer.MAX_VALUE;

        for (Ambulance ambulance :
                suitableAmbulances) {

            int capability =
                    getAmbulanceCapability(
                            ambulance.type);

            if (capability >= requiredCapability &&
                    capability < minimumCapability) {

                minimumCapability = capability;
            }
        }

        // -----------------------------------------------------
        // STEP 4:
        // Keep only ambulances with the selected capability
        // -----------------------------------------------------

        List<Ambulance> capabilityMatches =
                new ArrayList<>();

        for (Ambulance ambulance :
                suitableAmbulances) {

            int capability =
                    getAmbulanceCapability(
                            ambulance.type);

            if (capability == minimumCapability) {

                capabilityMatches.add(ambulance);
            }
        }

        // -----------------------------------------------------
        // STEP 5:
        // Select nearest ambulance
        // -----------------------------------------------------

        Ambulance best = null;

        for (Ambulance ambulance :
                capabilityMatches) {

            if (best == null) {

                best = ambulance;

            } else if (
                    ambulance.currentDistance <
                            best.currentDistance) {

                best = ambulance;
            }
        }

        return best;
    }

    // =========================================================
    // AUTOMATIC DISPATCH
    // =========================================================

    public void automaticallyDispatch() {

        while (!waitingQueue.isEmpty()) {

            Emergency emergency =
                    waitingQueue.peek();

            Ambulance bestAmbulance =
                    findBestAmbulance(emergency);

            /*
             * No suitable ambulance currently available.
             *
             * Emergency stays in queue.
             */

            if (bestAmbulance == null) {

                break;
            }

            waitingQueue.poll();

            try {

                dispatchEmergency(
                        emergency,
                        bestAmbulance);

            } catch (Exception e) {

                System.out.println(
                        "Dispatch failed: " +
                                e.getMessage());

                // Put emergency back in queue

                waitingQueue.add(emergency);

                break;
            }
        }
    }

    // =========================================================
    // DISPATCH EMERGENCY
    // =========================================================

    public void dispatchEmergency(
            Emergency emergency,
            Ambulance ambulance)
            throws AmbulanceUnavailableException,
            InvalidStateTransitionException {

        if (emergency == null) {

            throw new AmbulanceUnavailableException(
                    "Emergency cannot be null");
        }

        if (ambulance == null) {

            throw new AmbulanceUnavailableException(
                    "Ambulance cannot be null");
        }

        // -----------------------------------------------------
        // Check availability
        // -----------------------------------------------------

        if (ambulance.state !=
                AmbulanceState.AVAILABLE) {

            throw new AmbulanceUnavailableException(
                    "Ambulance " +
                            ambulance.ambulanceId +
                            " is not available");
        }

        // -----------------------------------------------------
        // Check suitability
        // -----------------------------------------------------

        if (!isSuitable(ambulance, emergency)) {

            throw new AmbulanceUnavailableException(
                    "Ambulance " +
                            ambulance.ambulanceId +
                            " is not suitable for " +
                            emergency.type +
                            " emergency");
        }

        // -----------------------------------------------------
        // Assign ambulance
        // -----------------------------------------------------

        ambulance.changeState(
                AmbulanceState.DISPATCHED);

        emergency.assignedAmbulance =
                ambulance;

        emergency.status =
                EmergencyStatus.ASSIGNED;

        /*
         * ETA calculation:
         *
         * Average speed = 40 km/h
         *
         * Time = Distance / Speed
         *
         * Convert hours to minutes:
         *
         * ETA = Distance / 40 * 60
         */

        emergency.estimatedArrivalMinutes =
                (ambulance.currentDistance / 40.0) * 60.0;

        emergencyHistory.add(
                "Emergency " +
                        emergency.emergencyId +
                        " assigned to ambulance " +
                        ambulance.ambulanceId);

        System.out.println(
                "DISPATCHED: " +
                        emergency.emergencyId +
                        " -> " +
                        ambulance.ambulanceId);

        System.out.println(
                "ETA: " +
                        emergency.estimatedArrivalMinutes +
                        " minutes");
    }

    // =========================================================
    // UPDATE AMBULANCE STATE
    // =========================================================

    public void updateAmbulanceState(
            String ambulanceId,
            AmbulanceState newState)
            throws InvalidStateTransitionException {

        Ambulance ambulance =
                findAmbulance(ambulanceId);

        if (ambulance == null) {

            throw new IllegalArgumentException(
                    "Ambulance not found: " +
                            ambulanceId);
        }

        AmbulanceState oldState =
                ambulance.state;

        ambulance.changeState(newState);

        emergencyHistory.add(
                "Ambulance " +
                        ambulanceId +
                        " state changed: " +
                        oldState +
                        " -> " +
                        newState);

        // -----------------------------------------------------
        // EN ROUTE
        // -----------------------------------------------------

        if (newState ==
                AmbulanceState.EN_ROUTE) {

            for (Emergency emergency :
                    emergencies) {

                if (emergency.assignedAmbulance ==
                        ambulance) {

                    emergency.status =
                            EmergencyStatus.IN_PROGRESS;

                    emergencyHistory.add(
                            "Emergency " +
                                    emergency.emergencyId +
                                    " is now IN PROGRESS");
                }
            }
        }

        // -----------------------------------------------------
        // PATIENT PICKED UP
        // -----------------------------------------------------

        if (newState ==
                AmbulanceState.PATIENT_PICKED_UP) {

            for (Emergency emergency :
                    emergencies) {

                if (emergency.assignedAmbulance ==
                        ambulance) {

                    emergencyHistory.add(
                            "Patient " +
                                    emergency.patientId +
                                    " picked up by " +
                                    ambulance.ambulanceId);
                }
            }
        }

        // -----------------------------------------------------
        // HOSPITAL ARRIVED
        // -----------------------------------------------------

        if (newState ==
                AmbulanceState.HOSPITAL_ARRIVED) {

            for (Emergency emergency :
                    emergencies) {

                if (emergency.assignedAmbulance ==
                        ambulance) {

                    emergency.status =
                            EmergencyStatus.COMPLETED;

                    emergencyHistory.add(
                            "Emergency " +
                                    emergency.emergencyId +
                                    " completed at " +
                                    emergency.destinationHospital);
                }
            }
        }

        // -----------------------------------------------------
        // AVAILABLE
        // -----------------------------------------------------

        if (newState ==
                AmbulanceState.AVAILABLE) {

            System.out.println(
                    "Ambulance " +
                            ambulanceId +
                            " is AVAILABLE again.");

            /*
             * Automatically check the waiting queue.
             */

            automaticallyDispatch();
        }
    }

    // =========================================================
    // FIND AMBULANCE
    // =========================================================

    private Ambulance findAmbulance(
            String ambulanceId) {

        if (ambulanceId == null ||
                ambulanceId.isBlank()) {

            return null;
        }

        for (Ambulance ambulance :
                ambulances) {

            if (ambulance.ambulanceId
                    .equalsIgnoreCase(ambulanceId)) {

                return ambulance;
            }
        }

        return null;
    }

    // =========================================================
    // DISPLAY AMBULANCES
    // =========================================================

    public void displayAmbulances() {

        System.out.println(
                "\n========== AMBULANCES ==========");

        if (ambulances.isEmpty()) {

            System.out.println(
                    "No ambulances available.");

            return;
        }

        for (Ambulance ambulance :
                ambulances) {

            System.out.println(ambulance);
        }
    }

    // =========================================================
    // DISPLAY EMERGENCIES
    // =========================================================

    public void displayEmergencies() {

        System.out.println(
                "\n========== EMERGENCIES ==========");

        if (emergencies.isEmpty()) {

            System.out.println(
                    "No emergencies registered.");

            return;
        }

        for (Emergency emergency :
                emergencies) {

            System.out.println(emergency);
        }
    }

    // =========================================================
    // DISPLAY WAITING QUEUE
    // =========================================================

    public void displayWaitingQueue() {

        System.out.println(
                "\n========== WAITING QUEUE ==========");

        if (waitingQueue.isEmpty()) {

            System.out.println(
                    "Waiting queue is empty.");

            return;
        }

        /*
         * Copy queue into a list so that we can
         * display it in priority order.
         */

        PriorityQueue<Emergency> copy =
                new PriorityQueue<>(
                        waitingQueue);

        while (!copy.isEmpty()) {

            Emergency emergency =
                    copy.poll();

            System.out.println(
                    emergency.emergencyId +
                            " | Patient: " +
                            emergency.patientId +
                            " | Priority: " +
                            emergency.type);
        }
    }

    // =========================================================
    // DISPLAY HISTORY
    // =========================================================

    public void displayHistory() {

        System.out.println(
                "\n========== EMERGENCY HISTORY ==========");

        if (emergencyHistory.isEmpty()) {

            System.out.println(
                    "No history available.");

            return;
        }

        int count = 1;

        for (String record :
                emergencyHistory) {

            System.out.println(
                    count + ". " + record);

            count++;
        }
    }

    // =========================================================
    // GETTERS FOR TESTING
    // =========================================================

    public Ambulance getAmbulance(String ambulanceId) {

        return findAmbulance(ambulanceId);
    }

    public Emergency getEmergency(String emergencyId) {

        if (emergencyId == null ||
                emergencyId.isBlank()) {

            return null;
        }

        for (Emergency emergency :
                emergencies) {

            if (emergency.emergencyId
                    .equalsIgnoreCase(emergencyId)) {

                return emergency;
            }
        }

        return null;
    }

    public int getWaitingQueueSize() {

        return waitingQueue.size();
    }

    public int getAmbulanceCount() {

        return ambulances.size();
    }

    public int getEmergencyCount() {

        return emergencies.size();
    }

    public int getHistorySize() {

        return emergencyHistory.size();
    }

    // =========================================================
    // MAIN METHOD
    // =========================================================

    public static void main(String[] args)
            throws Exception {

        AmbulanceSystem system =
                new AmbulanceSystem();

        // -----------------------------------------------------
        // DRIVERS
        // -----------------------------------------------------

        Driver driver1 =
                new Driver(
                        "Rahul",
                        "9876543210",
                        "DL001");

        Driver driver2 =
                new Driver(
                        "Arjun",
                        "9876543211",
                        "DL002");

        Driver driver3 =
                new Driver(
                        "Vikram",
                        "9876543212",
                        "DL003");

        // -----------------------------------------------------
        // AMBULANCES
        // -----------------------------------------------------

        Ambulance basic =
                new Ambulance(
                        "AMB001",
                        AmbulanceType.BASIC,
                        driver1,
                        5);

        Ambulance als =
                new Ambulance(
                        "AMB002",
                        AmbulanceType.ADVANCED_LIFE_SUPPORT,
                        driver2,
                        8);

        Ambulance icu =
                new Ambulance(
                        "AMB003",
                        AmbulanceType.ICU,
                        driver3,
                        12);

        system.addAmbulance(basic);
        system.addAmbulance(als);
        system.addAmbulance(icu);

        // -----------------------------------------------------
        // EMERGENCIES
        // -----------------------------------------------------

        Emergency criticalEmergency =
                new Emergency(
                        "E001",
                        "P1001",
                        EmergencyType.CRITICAL,
                        "Katpadi",
                        "CMC Hospital",
                        15);

        Emergency highEmergency =
                new Emergency(
                        "E002",
                        "P1002",
                        EmergencyType.HIGH,
                        "Vellore",
                        "Apollo Hospital",
                        10);

        Emergency normalEmergency =
                new Emergency(
                        "E003",
                        "P1003",
                        EmergencyType.NORMAL,
                        "Sathuvachari",
                        "CMC Hospital",
                        7);

        system.addEmergency(criticalEmergency);
        system.addEmergency(highEmergency);
        system.addEmergency(normalEmergency);

        // -----------------------------------------------------
        // DISPLAY
        // -----------------------------------------------------

        system.displayAmbulances();

        system.displayEmergencies();

        system.displayWaitingQueue();

        // -----------------------------------------------------
        // UPDATE AMBULANCE
        // -----------------------------------------------------

        System.out.println(
                "\n========== STATE UPDATES ==========");

        system.updateAmbulanceState(
                "AMB003",
                AmbulanceState.EN_ROUTE);

        system.updateAmbulanceState(
                "AMB003",
                AmbulanceState.PATIENT_PICKED_UP);

        system.updateAmbulanceState(
                "AMB003",
                AmbulanceState.HOSPITAL_ARRIVED);

        system.updateAmbulanceState(
                "AMB003",
                AmbulanceState.AVAILABLE);

        // -----------------------------------------------------
        // HISTORY
        // -----------------------------------------------------

        system.displayHistory();

        // -----------------------------------------------------
        // FINAL STATUS
        // -----------------------------------------------------

        System.out.println(
                "\n========== FINAL STATUS ==========");

        system.displayAmbulances();
        system.displayEmergencies();
    }
}