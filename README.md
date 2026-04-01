
## **BookCare: Sustainable Book Reuse Mobile Application**

### **📖 Overview**
**BookCare** is a mobile application designed to combat book waste by providing a structured platform for the donation and exchange of second-hand books. By fostering a sustainable reading community, BookCare supports **United Nations SDG 12: Responsible Consumption & Production**, specifically targeting the reduction of waste generation through reuse.

### **✨ Key Features**
* **User Authentication & Profile:** Secure registration and login via Firebase, featuring "Forgot Password" recovery and customizable UI themes (Green/Blue modes).
* **Book Marketplace:** A central hub to browse, search, and upload book listings categorized under "Exchange" or "Donation."
* **Smart Recommendation System:** Personalized book suggestions based on user-selected genres to promote the circular economy.
* **Donation & Free Exchange:** A streamlined request and confirmation workflow for transparent transactions.
* **Community & Review Forum:** An interactive space for users to share reviews, discuss literature, and build social trust through likes and comments.
* **Eco-Impact & Badge System:** A gamified incentive system that awards points (10 for donations, 5 for exchanges), allowing users to track their individual environmental contributions.

### **🛠️ Technical Stack**
* **Language:** Java
* **IDE:** Android Studio
* **Backend (Firebase):**
    * *Authentication:* Secure user access management.
    * *Realtime Database:* NoSQL JSON-based storage for instant data synchronization.
    * *Cloud Storage:* Storage for media and book assets.

### **🏗️ System Architecture**
The application utilizes a **Client-Server Model**:
1.  **Client Side:** Android Application (Activities & Fragments) handling UI and user interactions.
2.  **Network Layer:** Communication facilitated via the Firebase SDK/API.
3.  **Backend Side:** Google Firebase managing the database, storage, and authentication services.

### **📈 Performance & Scalability**
* **Usability:** High task completion rates with minimal user guidance.
* **Speed:** Optimized performance with main screens loading within approximately 2 seconds.
* **Scalability:** Verified stable performance for up to 500 concurrent users without feature degradation.
