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
        
        +showMap(Library library) Map
        
        +addMember(Member member, Library library) boolean
        +showMember(Member member) Member
    }

    class Library {
        -String name
        -List~Media~ media
        -List~Resource~ resources
        -Map map

        +getName() String

        +addMedia(Media media) boolean
        +showMedia(Media media) Media
        +removeMedia(Media media) boolean
        
        +addResource(Resource resource) boolean
        +showResource(Resource resource) Resource
    }

    class Member {
        -List~MediaCopy~ borrowedMedia
        -List~Resource~ resources
        -List~Constraint~ constraints
        
        +addMediaCopy(MediaCopy copy) boolean
        +addResource(Resource resource) boolean
        +addConstraint(Constraint constraint) boolean
    }
    
    class Constraint {
        -String constraint
    }

    class Media {
        -List~Review~ reviews
        -List~MediaCopy~ copies
        -Queue~Member~ waitlist
        
        +addReview(Review review) boolean
        +deleteReview(Review review) boolean
        +showReview(Review review) Review
        
        +addCopy(MediaCopy copy) boolean
        +addToWaitlist(Member member) boolean
    }

    class MediaCopy {
        -int copyNumber
    }
    
    class Resource {
        -List~Booking~ bookings

        +addBooking(Booking booking, Member member) boolean
        +deleteBooking(Booking booking, Member member) boolean
    }
    
    class Review {
        String review
    }
    
    class Booking {
        -Member member
        
        -String startTime
        -int startDay
        -int startMonth
        -int startYear

        -String endTime
        -int endDay
        -int endMonth
        -int endYear
    }
    
    class Map {
        -MapCell[][] grid
        -Set~Character~ legend
    }

    class MapCell {
        <<enumeration>>
        WALL,
        EMPTY,
        SCIENCE,
        ARTS,
        BIOGRAPHY,
        FANTASY,
        CRIME,
        HORROR
    }
 

    note for LibrarySystem "Invariant properties:
    <ul>
        <li>libraries != null
        <li>loop: no Libraries are null in libraries.
        
        <li>members != null
        <li>loop: no Members are null in members.
    </ul>"

    note for Library "Invariant properties:
    <ul>
        <li>media != null
        <li>loop: no Media are null in media.
        
        <li>resources != null
        <li>loop: no Resources are null in resources.
        
        <li>map != null
    </ul>"

    note for Member "Invariant properties:
    <ul>
        <li>borrowedMedia != null
        <li>loop: no Media are null in borrowedMedia.
        
        <li>constraints != null
        <li>loop: no Constraints are null in constraints.
    </ul>"

    note for Media "Invariant properties:
    <ul>
        <li>reviews != null
        <li>loop: no Reviews are null in reviews.
        
        <li>copies != null
        <li>loop: no MediaCopies are null in copies.
        
        <li>waitlist != null
        <li>loop: no Members are null in waitlist.
    </ul>"

    note for Resource "Invariant properties:
    <ul>
        <li>reviews != null
        <li>loop: no Reviews are null in reviews.
        
        <li>bookings != null
        <li>loop: no Bookings are null in bookings.
    </ul>"

    note for Booking "Invariant properties:
    <ul>
        <li>member != null
        
        <li>startTime != null && startTime.length() > 0
        <li>startDay != null && startDay.length() > 0
        <li>startMonth != null && startMonth.length() > 0
        <li>startYear != null && startYear.length() > 0
        
        <li>endTime != null && endTime.length() > 0
        <li>endDay != null && endDay.length() > 0
        <li>endMonth != null && endMonth.length() > 0
        <li>endYear != null && endYear.length() > 0
    </ul>"

    LibrarySystem --* Library
    LibrarySystem --* Member
    
    Library --* Map
    Library --* Media
    Library --* Resource
    
    Member --* Constraint
    Member --* Booking
    Member ..> MediaCopy
    Member ..> Resource
    
    Media --* MediaCopy
    Media --* Review
    Media ..> Member
    
    Resource --* Booking
    
    Map --* MapCell
```