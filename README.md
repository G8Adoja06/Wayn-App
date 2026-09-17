# Wayn? — Turn Anywhere Into an Adventure

Wayn? is an AI-powered social adventure app that turns a user’s location, budget, available time, transportation method, group size, and mood into personalized real-world quests.

Instead of simply recommending places, Wayn? creates a complete mission made up of multiple activities such as food challenges, exploration, photography, discovery, mystery tasks, and social experiences.

The goal is simple:

> **Turn anywhere into an adventure.**

---

## About the Project

The idea behind Wayn? comes from a very common question:

> **“Wayn badna nrou7?” — Where should we go?**

Rather than spending time deciding what to do, users can give Wayn? a few constraints and instantly receive a playable adventure that fits their situation.

For example:

- 4 people
- travelling by car
- $10 budget
- 1 hour available
- Beirut
- Adventure mood

Wayn? then creates a sequence of compatible quests while respecting the selected budget, time, transportation, and location.

---

## Main Features

### Personalized Adventures

Wayn? generates quests based on:

- Current or selected location
- Budget
- Available time
- Transportation method
- Group size
- Mood
- Interests

The generated quests are filtered so that they remain realistic and compatible with the user's restrictions.

---

### We’re Bored

The main experience starts with the **WE’RE BORED** button.

Users select:

1. Who is coming
2. Transportation
3. Budget
4. Available time
5. Preferred adventure style

Wayn? then creates a complete mission containing several quests.

---

## Special Modes

### Broke Mode

Designed for users with very limited budgets.

Available budget options focus on:

- $0 adventures
- low-cost activities
- walking
- photography
- exploration
- free landmarks
- discovery challenges

Arabic identity:

**على الصفر**

---

### Date Mode

Creates date-oriented adventures for couples or groups of two couples.

Options include:

**DUO**  
انت وصاحبتك

**QUAD**  
انت وصاحبتك، صاحبك وصاحبتو

Date missions may include scenic walks, food, photography, discovery, conversation challenges, and shared experiences.

Arabic identity:

**مغروم**

---

### Food Mode

Generates food-focused adventures according to the user's budget and available time.

Time options include:

**0–1 hr**  
لقمة عالسريع

**1+ hr**  
مطول استاذ؟

Arabic identity:

**جوعان**

---

## Transportation

Users choose how they want to travel.

### Walk

**ع اجريك يا بيك؟**

Quests remain within a reasonable walking distance.

### Car

**مرتاح عوضعك بالبنزين؟**

Allows missions covering a wider area while still respecting the selected time.

---

## Quest System

Each mission contains multiple quests based on the selected duration.

Typical mission lengths:

| Available Time | Number of Quests |
|---|---:|
| 30 minutes | 2–3 |
| 1 hour | 3–4 |
| 2 hours | 4–6 |
| 3+ hours | 5–8 |

Possible quest categories include:

- Food
- Photography
- Discovery
- Exploration
- Mystery
- Culture
- Walking
- Outdoor activities
- Social challenges

A constraint-validation system ensures that generated quests match the user's budget, location, transportation, and available time.

---

## Budget Tracking

Wayn? tracks the real cost of an adventure.

After completing every quest, the user enters the amount actually spent.

The application then calculates:

```text
Remaining Budget = Starting Budget - Total Amount Spent
```

At the end of the mission, users can see:

* Starting budget
* Total amount spent
* Remaining budget

This allows Wayn? to compare the planned adventure with the user's actual spending.

---

## Plot Twists

Missions can unexpectedly change through the **Plot Twist** system.

Examples include:

* Timed challenges
* Bonus XP
* Photography challenges
* Mystery clues
* Walking-only sections
* Side quests
* Destination changes

Plot Twists are always validated against the user's original constraints so that they do not exceed the selected budget, time, or transportation limits.

---

## Wayn Roulette

Wayn Roulette allows users to spin a wheel and randomly select an adventure style.

Possible results include:

* Chaos
* Food
* Chill
* Broke
* Adventure
* Mystery
* Wild Card

The visual segment selected by the wheel directly corresponds to the resulting mission type.

---

## Camera Proof

Certain quests allow users to submit proof using the phone's camera.

Users can:

* open the real Android camera
* capture a photo
* return to the mission
* attach the photo to the quest

Photo-based quests can also receive playful scores and comments.

---

## Quest Feed

Wayn? includes a local social feed where users can view completed adventures.

Feed posts can include:

* Quest title
* Location
* XP earned
* Money spent
* Photo
* Caption
* Likes
* Comments

Users can also press:

**TRY QUEST**

to add another user's quest to their own available adventures.

---

## Quest Parties

Quest Parties allow users to create or join adventure groups.

A party can include:

* Location
* Number of available spots
* Transportation method
* Budget
* Time
* Quest type
* Group chat

For the prototype/demo version, social activity is simulated locally without requiring a backend.

---

## Explore

The Explore section contains a curated library of quests for multiple Lebanese cities.

Supported locations include:

* Beirut
* Jounieh
* Byblos
* Batroun
* Zahle
* Tripoli
* Sidon

Quests are filtered according to:

* City
* Budget
* Duration
* Transportation
* Category
* Group size

The same quest catalog is used by the adventure generator to keep mission generation consistent and relevant.

---

## Daily Quests & Streaks

Wayn? generates a personalized daily quest.

Completing at least one qualifying quest per day increases the user's streak.

Milestones provide XP rewards and achievements.

---

## XP & Levels

Users earn XP by completing quests and missions.

Example progression:

1. Lost Tourist
2. Weekend Rookie
3. Local Scout
4. Street Explorer
5. Chaos Specialist
6. Beirut Veteran
7. Wayn Legend

---

## Achievements

Achievements include Bronze, Silver, and Gold tiers.

Examples:

### Broke But Alive

Complete $0 adventures.

### Chaos Merchant

Complete Chaos missions.

### Touch Grass

Complete outdoor quests.

### Main Character

Earn high photo-challenge scores.

### Suspiciously Local

Complete multiple adventures.

---

## Wayn DNA

Wayn DNA automatically analyzes the user's behavior.

It is calculated from:

* Completed quests
* Preferred categories
* Spending habits
* Transportation choices
* Adventure styles
* Exploration behavior
* Social activity

Example dimensions:

```text
Adventure  82%
Food       94%
Culture    52%
Chaos      78%
Chill      31%
Budget     86%
```

The percentages are generated from actual app activity and are not manually editable.

---

## Logbook

Completed missions are automatically stored in the user's Logbook.

Each entry can include:

* Mission name
* Date
* Location
* Mode
* Transportation
* Duration
* XP
* Starting budget
* Actual spending
* Remaining budget
* Completed quests
* Final rank
* Photos

---

## Mission Persistence

An active adventure is not lost if the user:

* leaves the mission screen
* changes tabs
* backgrounds the app
* locks the phone
* receives a call

Mission state and timing are preserved so the user can return and continue through **Resume Mission**.

---

## Technology

Wayn? is built as a native Android application using:

* **Kotlin**
* **Jetpack Compose**
* **Material 3**
* **Navigation Compose**
* **ViewModel**
* **StateFlow**
* **Kotlin Coroutines**
* **DataStore**
* **Room**

The application was prototyped and developed using **Google AI Studio**.

---

## Offline / Demo Architecture

The current version is designed to work reliably during a live demo without requiring a backend.

Features such as:

* Quest Feed
* Quest Parties
* Leaderboards
* Social interactions

use local or seeded demo data.

The architecture can later be extended with a real backend such as Firebase or another cloud service.

---

## Design Identity

Wayn? uses a modern dark interface inspired by Beirut and Mediterranean nightlife.

The visual identity includes:

* Midnight backgrounds
* Electric orange accents
* Warm yellow highlights
* Bold typography
* Playful Lebanese Arabic
* Animated mission cards
* Haptic interactions
* Gamified progress

The app is designed to feel like a consumer social product rather than a traditional recommendation tool.

---

## Project Goal

Wayn? aims to reduce the friction of deciding what to do and encourage users to explore their surroundings in a spontaneous, affordable, and social way.

Instead of asking:

> “Where should we go?”

Wayn? asks:

> **“What adventure are we doing next?”**

---

## Future Development

Potential future improvements include:

* Real-time multiplayer Quest Parties
* Cloud-backed Quest Feed
* Live user accounts
* Real business and venue integrations
* AI-generated missions using live data
* Maps and routing
* Real-time events
* Friend systems
* Push notifications
* iOS version
* Expansion outside Lebanon

---

## Team

Developed as part of an AI application challenge.

---

## License

This project is currently intended for educational, prototype, and competition purposes.

---

# Wayn?

**Turn anywhere into an adventure.**
