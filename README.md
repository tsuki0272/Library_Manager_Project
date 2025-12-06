---
title: Alem's Library System
author: Alemtuba Longkumer (longkuma@myumanitoba.ca)
date: Fall 2025
---

# Domain model

## Resources

* I got inspiration for some book categories here: <https://www.bookbeaver.co.uk/blog/different-types-of-books>
* I got info on what info bookings require from here: <https://riverlanding.com/blog/what-does-a-hotel-require-from-you-to-book-a-room/>

## Diagrams

### Sign In
```mermaid
flowchart
    subgraph LOGIN
        login[[LOGIN SCREEN]]
        login ==New account==> registerAccount
        login ==Existing account==> enterCredentials

        enterCredentials[Enter username and password]
        enterCredentials == username and password ==> processCredentials
        
        processCredentials{Valid credentials?}
        processCredentials -. Valid .-> loggedIn
        processCredentials -. Invalid .-> enterCredentials
        
        registerAccount[Register new account]
        registerAccount == Back ==> login
        registerAccount == New Username and Password ==> validNewAccount
        
        validNewAccount{Valid?}
        validNewAccount -. Valid .-> createdSuccess
        validNewAccount -. Invalid .-> registerAccount
        
        createdSuccess[Successfully created]
        createdSuccess -. Back .-> login
        
        loggedIn[[LOGGED IN SUCCESSFULLY]]
    end
```

### Find Path
```mermaid
flowchart
    subgraph FINDING PATH
    start[[START]]

    start --> chooseType{Search for MEDIA or RESOURCE?}

    chooseType -- MEDIA --> mediaCoordinates{Does this media exist<br>in the map database?}

    mediaCoordinates -- No --> mediaNotFound[MEDIA NOT FOUND] --> endMedia1[[END]]

    mediaCoordinates -- Yes --> beginSearchMedia[BEGIN PATH SEARCH]

    beginSearchMedia --> exploreMapMedia[Explore surrounding map<br>step-by-step]
    exploreMapMedia --> reachableMedia{Can a path be found<br>from the start location<br>to this media?}

    reachableMedia -. No .-> noPathMedia[NO PATH AVAILABLE] --> endMedia2[[END]]

    reachableMedia -. Yes .-> buildPathMedia[Build movement path<br>from start to media]

    buildPathMedia --> displayMediaPath[RETURN PATH TO MEDIA] --> finalMedia[[END]]

    chooseType -- RESOURCE --> resourceCoordinates{Does this resource exist<br>in the map database?}

    resourceCoordinates -- No --> resourceNotFound[RESOURCE NOT FOUND] --> endRes1[[END]]

    resourceCoordinates -- Yes --> beginSearchRes[BEGIN PATH SEARCH]

    beginSearchRes --> exploreMapRes[Explore surrounding map<br>step-by-step]
    exploreMapRes --> reachableRes{Can a path be found<br>from the start location<br>to this resource?}

    reachableRes -. No .-> noPathRes[NO PATH AVAILABLE] --> endRes2[[END]]

    reachableRes -. YES .-> buildPathRes[Build movement path<br>from start to resource]

    buildPathRes --> displayResPath[RETURN PATH TO RESOURCE] --> finalRes[[END]]
    
    end
```

### Borrow Media
```mermaid
flowchart
    subgraph BORROWING MEDIA
        mainMenu[[MAIN MENU]]
        mainMenu == Open Media Borrowing List ==> constraintStatus

        constraintStatus{Member has constraints?}
        constraintStatus -. Yes .-> unableToBorrow
        constraintStatus -. No .-> selectMedia

        unableToBorrow[[UNABLE TO BORROW]]
        unableToBorrow -. Back to Main Menu .-> mainMenu

        selectMedia[Media List]
        selectMedia -. Select Media .-> mediaOptions

        mediaOptions{Media Available?}
        mediaOptions -. Yes .-> borrowMedia
        mediaOptions -. No .-> waitlistMedia

        waitlistMedia[Waitlist]

        borrowMedia[[BORROWED SUCCESSFULLY]]
    end
```

### Return Media
```mermaid
flowchart
    subgraph RETURNING MEDIA
        selectMedia[[BORROWED MEDIA LIST]]
        selectMedia == Select Media to Return ==> returnDecisions

        returnDecisions[Media Decisions]
        returnDecisions == Read Review ==> reviewReader
        returnDecisions == Return media ==> returnMedia
        returnDecisions == Write Review ==> reviewWriter

        reviewReader[Read Reviews]
        reviewReader == Back to Media Selection ==> selectMedia
        reviewReader == Back to Media Decisions ==> returnDecisions

        reviewWriter{Is review text empty?}
        reviewWriter -. Not Empty .-> successfulReviewWrite
        reviewWriter -. Empty .-> returnDecisions

        successfulReviewWrite[Written]
        successfulReviewWrite == Back to Media Selection ==> selectMedia
        successfulReviewWrite == Back to Media Decisions ==> returnDecisions

        returnMedia[[RETURNED SUCCESSFULLY]]
    end
```

### Book Resource
```mermaid
flowchart
    subgraph BOOK RESOURCE
        bookResource[[BOOK RESOURCE]]
        bookResource== Select Day ==>dayAvailability

        dayAvailability{Available for selected day?}
        dayAvailability -. Available spots .-> successfulBooking
        dayAvailability -. No available spots .-> bookResource

        successfulBooking[[BOOKING SUCCESS]]
    end
```

Here is the diagram for my domain model

```mermaid
classDiagram

    class LibrarySystem {
        -List~Library~ libraries
        -Set~Member~ members

        +addLibrary(Library library) boolean
        +showLibrary(Library library) Library
        +addMember(Member member) boolean
        +showMember(String name) Member
        +getLibraries() List~Library~
    }

    class Library {
        -String name
        -List~Media~ media
        -List~Resource~ resources
        -Map map

        +getName() String
        +getMedia() List~Media~
        +getResources() List~Resource~
        +getMap() Map
        +addMedia(Media media) boolean
        +addResource(Resource resource) boolean
    }

    class Member {
        -String name
        -String password
        -List~MediaCopy~ borrowedMedia
        -List~Resource~ bookedResources
        -List~Constraint~ constraints

        +getName() String
        +getPassword() String
        +getBorrowedMedia() List~MediaCopy~
        +getBookedResources() List~Resource~
        +getconstraints() List~Constraint~
        +hasConstraints() boolean
        +addBorrowedCopy(MediaCopy copy) void
        +removeBorrowedCopy(MediaCopy copy) void
        +addConstraint(Constraint c) void
        +addBookedResource(Resource r) void
        +bookResource(Resource r, String dateString, String timeString) boolean
    }

    class Constraint {
        -String constraint
        +getConstraint() String
    }

    class Media {
        -String title
        -String author
        -int[] coordinates
        -List~MediaCopy~ copies
        -List~Review~ reviews

        +addCopy(MediaCopy copy) boolean
        +getCoordinates() int[]
        +getAuthor() String
        +getTitle() String
        +addReview(Review review) boolean
        +getReviews() List~Review~
        +getCopies() List~MediaCopy~
        +findAvailableCopy() MediaCopy
    }

    class MediaCopy {
        -int copyNumber
        -Media media
        -boolean borrowed
        -Member borrowedBy
        -String dueTime
        -String dueDate

        +isAvailable() boolean
        +markBorrowed(Member member, String dueTime, String dueDate) void
        +markReturned() void
        +getMedia() Media
        +getDueTime() String
        +getDueDate() String
    }

    class Resource {
        -String id
        -String resourceName
        -String openingTime
        -String closingTime
        -int timeslotLength
        -List~int[]~ coordinates
        -List~Booking~ bookings

        +getId() String
        +getResourceName() String
        +getOpeningTime() String
        +getClosingTime() String
        +getTimeslotLength() int
        +getCoordinates() List~int[]~
        +getBookings() List~Booking~
        +addBooking(Booking booking) boolean
        +isBookable() boolean
    }

    class Review {
        -String review
        +getReview() String
    }

    class Booking {
        -Member member
        -String startTime
        -String endTime
        -int day
        -int month
        -int year

        +validTime(String time) boolean
        +getMember() Member
        +getStartTime() String
        +getEndTime() String
        +getDay() int
        +getMonth() int
        +getYear() int
    }

    class Map {
        -char[][] grid
        -String[] legend
        -int[] kioskCoordinates
        -List~int[]~ mediaCoordinates
        -List~List~int[]~~ resourceCoordinates

        +gridFromString(Sting mapData) char[][]
        +getGrid() char[][]
        +getLegend() String[]
        +getKioskCoordinates() int[]
        +getMediaCoordinates() List~int[]~
        +addMediaCoordinates(int[] coords) boolean
        +addResourceCoordinates(List~int[]~ coords) boolean
    }

    class LinkedListStack~T~ {
        -Node head
        -int size
        
        +push(T item) void
        +pop() T
        +size() int
        +isEmpty() boolean
        +peek() T
    }

    class Node {
        -T data
        -Node next
    }

    

    LibrarySystem --* Library
    LibrarySystem --* Member

    Library --* Map
    Library --* Media
    Library --* Resource

    Member --* Constraint
    Member --> MediaCopy
    Member --> Resource

    Media --* MediaCopy
    Media --* Review
    Media --> Member
    Media --> Member

    Resource --* Booking
    Booking --> Member

    MediaCopy --> Media
    MediaCopy --> Member
    
    LinkedListStack --* Node
```