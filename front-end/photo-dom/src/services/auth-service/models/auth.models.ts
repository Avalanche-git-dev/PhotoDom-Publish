export class User {
    constructor(
      public id: string,
      public username: string,
      public role: string
    ) {}
  }
  
  export class TokenSet {
    constructor(
      public accessToken: string,
      public refreshToken: string,
      public idToken?: string
    ) {}
  }
  