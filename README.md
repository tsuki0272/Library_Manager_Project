---
title: Alem's Library System
author: Alemtuba Longkumer (longkuma@myumanitoba.ca)
date: Fall 2025
---

# Domain model

## Resources

* I got inspiration for some book categories here: <https://www.bookbeaver.co.uk/blog/different-types-of-books>
* I got info on what info bookings require from here: <https://riverlanding.com/blog/what-does-a-hotel-require-from-you-to-book-a-room/>

## Diagram

Here is the diagram for my domain model

```mermaid
classDiagram

    class LibrarySystem {
        -List~Library~ libraries
        -Set~Member~ members
        
        +addLibrary(Library library) boolean
        +showLibrary(Library library) boolean
        
        +showMap(Library library) boolean
        
        +addMember(Member member) boolean
        +showMember(Member member) boolean
    }

    class Library {
        -List~Media~ media
        -List~Resource~ resources
        -Map map

        +addMedia(Media media) boolean
        +showMedia(Media media) boolean
        +removeMedia(Media media) boolean
        
        +addResource(Resource resource) boolean
        +showResource(Resource resource) boolean
    }

    class Member {
        -List~MediaCopy~ borrowedMedia
        -List~Resource~ resources
        -List~Constraint~ constraints
        -ContactInfo contactInfo
        
        +addMediaCopy(MediaCopy copy) boolean
        +addResource(Resource resource) boolean
        +addConstraint(Constraint constraint) boolean
        +editContactInfo() boolean
    }
    
    class ContactInfo {
        -String name
        -String phoneNumber
        -String emailAddress
        
        +setName(String name) boolean
        +setPhoneNumber(String number) boolean
        +setEmailAddress(String email) boolean
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
        +showReview(Review review) boolean
        
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
    </ul>"

    note for Library "Invariant properties:
    <ul>
        <li>members != null
        <li>loop: no Members are null in members.
        
        <li>media != null
        <li>loop: no Media are null in media.
        
        <li>resources != null
        <li>loop: no Resources are null in resources.
        
        <li>map != null
    </ul>"

    note for Member "Invariant properties:
    <ul>
        <li>borrowedMedia != null
        <li>loop: no Media are null in borrowMedia.
        
        <li>constraints != null
        <li>loop: no Constraints are null in constraints.
        
        <li>contactInfo != null
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
    
    note for ContactInfo "Invariant properties:
    <ul>
        <li>name != null && name.length() > 0
        <li>phoneNumber != null && phoneNumber.length() > 0
        <li>emailAddress != null && emailAddress.length() > 0
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
    
    Member ..> MediaCopy
    Member --* ContactInfo
    Member ..> Resource
    Member --* Constraint
    Member --* Booking
    
    Media --* MediaCopy
    Media ..> Member
    Media --* Review
    
    Resource --* Booking
    
    Map --* MapCell
```