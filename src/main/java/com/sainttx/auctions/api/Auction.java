/*
 * Copyright (C) SainttX <http://sainttx.com>
 * Copyright (C) contributors
 *
 * This file is part of Auctions.
 *
 * Auctions is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Auctions is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Auctions.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.sainttx.auctions.api;

import com.sainttx.auctions.api.module.AuctionModule;
import com.sainttx.auctions.api.reward.Reward;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.UUID;

/**
 * Represents an Auction that players can bid on
 */
public interface Auction {

    /**
     * Gets the owner of this auction
     *
     * @return the auction creator
     */
    UUID getOwner();

    /**
     * Gets the name of the owner of this auction
     *
     * @return the auction creators name
     */
    String getOwnerName();

    /**
     * Gets whether or not there have been bids placed on this auction
     *
     * @return true if somebody has bid
     */
    boolean hasBids();

    /**
     * Gets whether or not the auction has ended
     *
     * @return true if the auction is over
     */
    boolean hasEnded();

    /**
     * Gets the {@link UUID} of the current top bidder for this auction
     *
     * @return the current {@link UUID} of the winner
     */
    UUID getTopBidder();

    /**
     * Gets the name of the current top bidder
     *
     * @return the top bidders name
     */
    String getTopBidderName();

    /**
     * Gets the reward that is being auctioned
     *
     * @return this auctions reward
     */
    Reward getReward();

    /**
     * Returns what type of auction this is
     *
     * @return the auction type of this auction
     */
    AuctionType getType();

    /**
     * Gets the amount of time left in this auction
     *
     * @return amount of time left
     */
    int getTimeLeft();

    /**
     * Sets the amount of time left in this auction
     *
     * @param time new amount of time left
     */
    void setTimeLeft(int time);

    /**
     * Starts the auction
     */
    void start();

    /**
     * Stops this auction and returns the money to the top bidder.
     * Rewards are left unhandled and are thrown unless other action
     * is taken by plugin implementations
     */
    void impound();

    /**
     * Cancels this auction and returns the items to the owner
     */
    void cancel();

    /**
     * Ends the auction as if the timer ran out
     *
     * @param broadcast whether or not to broadcast any
     * information about this auction ending
     */
    void end(boolean broadcast);

    /**
     * Gets the lowest amount that can be bid on this auction
     *
     * @return the bid increment of this auction
     */
    double getBidIncrement();

    /**
     * Returns the auctions autowin. Returns -1 if autowin was not set.
     *
     * @return how much money is required to automatically win the auction
     */
    double getAutowin();

    /**
     * Gets the percentage of money that will be removed from
     * the winning amount
     *
     * @return the tax percent of this auction
     */
    double getTax();

    /**
     * Gets the current tax impact on the top bid
     *
     * @return the current dollar amount that will be removed
     * from the winnings as a result of tax
     */
    double getTaxAmount();

    /**
     * Gets the current top bid in this auction
     *
     * @return the top bid
     */
    double getTopBid();

    /**
     * Gets the starting price of this auction
     *
     * @return the starting price
     */
    double getStartPrice();

    /**
     * Handles the event that a player places a bid.
     *
     * @param player the player
     * @param bid the amount bid by the player
     */
    void placeBid(Player player, double bid);

    /**
     * Gets a deep copy of modules present in this auction
     *
     * @return all modules tied to the auction
     */
    Collection<AuctionModule> getModules();

    /**
     * Adds a module to this auction
     *
     * @param module the module
     */
    void addModule(AuctionModule module);

    /**
     * Removes a module from this auction. Returns whether a
     * module was actually removed or not.
     *
     * @param module the module
     * @return if a module was actually removed
     */
    boolean removeModule(AuctionModule module);

    /**
     * Represents an auctions timer
     */
    interface Timer extends Runnable {

    }

    /**
     * Represents an Auction builder
     */
    interface Builder {

        /**
         * Creates the auction
         *
         * @return the auction created by the builder
         */
        Auction build();

        /**
         * Sets the initial owner of the auction. If this is
         * set to null the plugin will think that the auction is
         * being created by the console/server.
         *
         * @param owner the player auctioning the item
         * @return this builder instance
         */
        Builder owner(Player owner);

        /**
         * Sets the bid increment of the auction that will be created
         *
         * @param increment the new bid increment
         * @return this builder instance
         */
        Builder bidIncrement(double increment);

        /**
         * Sets the auction start time of the auction that will be created
         *
         * @param time the new auction start time
         * @return this builder instance
         */
        Builder time(int time);

        /**
         * Sets the reward of the auction that will be created
         *
         * @param reward the new auctioned reward
         * @return this builder instance
         */
        Builder reward(Reward reward);

        /**
         * Sets the starting top bid of the auction that will be created
         *
         * @param bid the new top bid
         * @return this builder instance
         */
        Builder topBid(double bid);

        /**
         * Sets the autowin amount of the auction that will be created
         *
         * @param autowin the new autowin amount
         * @return this builder instance
         */
        Builder autowin(double autowin);
    }
}
