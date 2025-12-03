# Feature: Bio Character Counter and Profile Completion Indicator

## Description
This feature enhances the user profile page by adding:
1. **Bio Character Counter**: A real-time character counter showing current/max characters (e.g., "150/500 characters") with visual feedback when approaching the limit
2. **Profile Completion Indicator**: A percentage indicator showing how complete a user's profile is based on filled fields
3. **Bio Length Validation**: Server-side validation to enforce a 500 character limit on bio text

## Motivation
- Improves user experience by providing real-time feedback on bio length
- Helps users understand how complete their profile is
- Prevents errors by validating bio length before submission
- Encourages users to complete their profiles

## Changes Made
- Added `MAX_BIO_LENGTH` constant (500 characters) in `UserProfileInteractor`
- Added bio length validation in `UserProfileInteractor.execute()`
- Extended `UserProfileState` to track:
  - `bioCharacterCount`: Current number of characters in bio
  - `profileCompletionPercentage`: Calculated completion percentage
- Added `calculateCompletionPercentage()` method that considers:
  - Display name
  - Bio
  - Favorite Pokemon
  - Profile photo
  - Banner image
  - Score
- Updated `UserProfileView` to display:
  - Character counter below bio field with color coding (gray → orange → red)
  - Profile completion percentage with color coding (orange → blue → green)
- Real-time updates as user types in bio field

## Testing
- [ ] Verify character counter updates in real-time as user types
- [ ] Verify counter turns orange at 90% of limit (450 characters)
- [ ] Verify counter turns red when exceeding limit
- [ ] Verify profile completion percentage updates when fields are filled
- [ ] Verify bio validation prevents saving when over 500 characters
- [ ] Verify error message displays when bio exceeds limit

## Screenshots
(Add screenshots of the new UI elements)

## Related Issues
N/A

